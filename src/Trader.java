package projectLS;

public class Trader {
    /*
     * The Trader class serves as a pseudo Arb Trader
     * Each time a trade that could move the market in a way that has opportunity 
     * happens, Trader checks if one exists and executes a trade if it does. 
     */
    
    public poolStruct pool1;
    public poolStruct pool2;
    public double recentlyBoughtEthPrice;
    public double totalProfit;
   
    public Trader(poolStruct pool1, poolStruct pool2) {
        this.pool1 = pool1;
        this.pool2 = pool2;
        this.totalProfit = 0;
    }
    
    
    /*
     * Description: Pseudo (ie. meant to simulate) the concept of a trader only checking for arb oppertunities when it should
     * 
     * Inputs: 
     *          move -- an integer that represents whether a recent market update could have moved the market
     *          a 1 implies it could have
     *          
     *          telemetry -- a boolean for testing purposes, this tells the system to print market updates as they happen
     * 
     */
    public void scanRecentMove(int move, boolean telemetry) {
        //this method is pseudo for the concept of a trader only checking for arb oppertunities when it should.
        //we arbitrarliy say that if this function gets passed a 0, it implies someone recently provided liquidity to a pool
        //and we arbitrarily say that if it gets passed a 1, someone recently traded on a pool.
        
        if(move == 1) {
            scanArbOpportunity(telemetry);
        }
    }
    
    
    /*
     * Description: Scans the market to see if there is a positive profit arbitrage opportunity. If one exists,
     * a call to executeArb will be made which will make the trade
     * 
     * Inputs:
     *          telemetry -- a boolean for testing purposes, this tells the system to print market updates as they happen
     * 
     */
    public void scanArbOpportunity(boolean telemetry) {
        double bestEthPurchase;
        double ethOut;
        
        if((pool1.dai / pool1.eth) > (pool2.dai / pool2.eth)){
            bestEthPurchase = (997000 * Math.sqrt(pool1.dai * pool1.eth * pool2.dai * pool2.eth) - (1000000 * pool1.eth * pool2.dai))/
                    (997000 * pool2.dai + 994009 * pool1.dai);
            
            if(bestEthPurchase < 0) {
                return;
            }
            
            ethOut = pool2.eth - ((pool2.eth * pool2.dai) / (pool2.dai + ((pool1.dai - ((pool1.dai * pool1.eth) / (pool1.eth + 
                    (bestEthPurchase * 0.997)))) * 0.997)));
            
            if(ethOut > bestEthPurchase) { 
                executeArb(1, bestEthPurchase, telemetry);
            }
        } else {
            bestEthPurchase = (997000 * Math.sqrt(pool2.dai * pool2.eth * pool1.dai * pool1.eth) - (1000000 * pool2.eth * pool1.dai))/
                    (997000 * pool1.dai + 994009 * pool2.dai);
            
            if(bestEthPurchase < 0) {
                return;
            }
            
            ethOut = pool1.eth - ((pool1.eth * pool1.dai) / (pool1.dai + ((pool2.dai - ((pool2.dai * pool2.eth) / (pool2.eth + 
                    (bestEthPurchase * 0.997)))) * 0.997)));
            
            if(ethOut > bestEthPurchase) {
                executeArb(2, bestEthPurchase, telemetry);
            }
        }
    }
    
    
    /*
     * Description: Executes the trade that was calculated by scanArbOpportunity
     * 
     * Inputs:
     *          pool -- tells which pool we are going to sell Eth in. By the nature of the system, we'd buy in the opposite pool.
     *          bestEthPurchase -- gives the optimal quantity of Eth to buy in the pool
     *          telemetry -- a boolean for testing purposes, this tells the system to print market updates as they happen
     * 
     */
    public void executeArb(int pool, double bestEthPurchase, boolean telemetry) {
        recentlyBoughtEthPrice = bestEthPurchase;
        double resultingDai;
        double resultingProfit;
        if(pool == 1) {
            resultingDai = this.pool1.tradeEth(bestEthPurchase);
            resultingProfit = this.pool2.tradeDai(resultingDai) - bestEthPurchase;
            totalProfit += resultingProfit;
            
        } else {
            resultingDai = this.pool2.tradeEth(bestEthPurchase);
            resultingProfit =this.pool1.tradeDai(resultingDai) - bestEthPurchase;
            totalProfit += resultingProfit;

        }
        
        if(telemetry) {
            tradeExecutionOutput(pool, bestEthPurchase, resultingDai, resultingProfit);
            System.out.print("Pool 1 ");
            this.pool1.printCurrState();
            
            System.out.print("Pool 2 ");
            this.pool2.printCurrState();
        }
        
        
    }
    
    
    /*
     * Description: If telemetry is true, this method gets called, which outputs the details of a trade and the state of the 
     *              market as a result of said trade
     * 
     * Inputs:
     *          pool -- the pool that the trade sold from
     *          bestEthPurchase -- the quantity of Eth sold in that pool
     *          resultingDai -- the quantity of Dai that resulted in the sale of Eth in said pool
     *          resultingProfit -- the quantity of Eth that resulted in the sale of the Dai, minus the bestEthPurchase amount
     * 
     */
    public void tradeExecutionOutput(int pool, double bestEthPurchase, double resultingDai, double resultingProfit) {
        System.out.println("Trader executed arbitrage opportunity, buying " + bestEthPurchase + " Eth from Pool" + pool + ", exchanging it for "
                + resultingDai + " Dai, and Selling it for a " + resultingProfit + " (Eth) profit in other pool");
        System.out.println();
    }

}
