package projectLS;

public class poolStruct {
    
    /*
     * poolStruct class that represents a pool
     */
    
    double dai;
    double eth;
    double swapFee;
    double outstandingLPtokens;
    
    public poolStruct (double dai, double eth, double swapFee) {
        this.dai = dai;
        this.eth = eth;
        this.swapFee = swapFee;
        this.outstandingLPtokens = 0; 
    }
    
    /*
     * Description: performs the add operation on the pool, where someone can 
     * add tokens to the pool, and is rewarded LPtokens. Punishes users from 
     * adding improper ratios of Dai & Eth
     * 
     * Inputs: 
     *          inputDai -- Quantity of Dai inputed
     *          inputEth -- Quantity of Eth inputed
     * 
     * Outputs:
     *          Resulting quantity of LP tokens
     * 
     */
    
    public double add(double inputDai, double inputEth) {
        this.dai += inputDai;
        this.eth += inputEth;
        
        //if ratio of inputs doesn't represent the ratio of the current pool
        //or if this is the first person to add to pool
        if(dai == 0 && eth == 0) {
            this.outstandingLPtokens += Math.sqrt(inputDai * inputEth);
            return Math.sqrt(inputDai * inputEth);
        }
        else if(dai / eth < inputDai/inputEth) {
            inputDai = (dai / eth) * inputEth;

        } else if (dai / eth > inputDai/inputEth) {
            inputEth = (eth / dai) * inputDai;
        } 
            
        this.outstandingLPtokens += Math.sqrt(inputDai * inputEth);
        return Math.sqrt(inputDai * inputEth);
        
    }
     
    
    /*
     * Description: performs the remove operation on the pool, where someone
     * can "cash in" their LP tokens for the equivalent quantities of Eth & Dai
     * 
     * Inputs:
     *          LPtokens -- quantity of LPtokens to cash in
     * Outputs:
     *          A double array with values {quantityDai, quantityEth} 
     *          or null if too many LPtokens are requested
     */
    public double[] remove(double LPtokens) {
        double[] daiEthPair = new double[2];
        
        if(LPtokens > outstandingLPtokens) {
            return null;
        }
        
        double proportionOwned = LPtokens / this.outstandingLPtokens;
        daiEthPair[0] = dai * proportionOwned;
        daiEthPair[1] = eth * proportionOwned;
        this.dai -= daiEthPair[0];
        this.eth -= daiEthPair[1];
        this.outstandingLPtokens -= LPtokens;
        
        return daiEthPair;
    }
    
    
    /*
     * Description: Models the action of trading Dai for Eth in the pool 
     * 
     * Inputs:
     *          daiAmt -- represents the amount of Dai that is requested to be traded
     * Outputs:
     *          A double amount of Eth that corresponds to that amount of Dai, if the pool
     *          cannot handle the trade, -1 is returned. 
     */
    public double tradeDai(double daiAmt) {
        
        double fee = daiAmt * swapFee;
        double daiAfterFee = daiAmt - fee;
        double newDai = dai + daiAfterFee;
        double newEth = (dai * eth) / newDai;
        if(newEth > eth) {
            System.out.println("attempting to swap for an unexisting amount");
            return -1;
        }
        double recievedEth = eth - newEth;
        
        this.eth = newEth;
        this.dai += daiAmt;
        return recievedEth;
    }
    
    
    /*
     * Description: Models the action of trading Eth for Dai in the pool 
     * 
     * Inputs:
     *          ethAmt -- represents the amount of Eth that is requested to be traded
     * Outputs:
     *          A double amount of Dai that corresponds to that amount of Eth, if the pool
     *          cannot handle the trade, -1 is returned. 
     */
    public double tradeEth(double ethAmt) {
        double fee = ethAmt * swapFee;
        double ethAfterFee = ethAmt - fee;
        double newEth = eth + ethAfterFee;
        double newDai = (dai * eth) / newEth;
        if(newDai > dai) {
            System.out.println("attempting to swap for an unexisting amount");
            return -1;
        }
        double recievedDai = dai - newDai;
        
        this.eth += ethAmt;
        this.dai = newDai;
        return recievedDai;
    
    }
    
    /*
     * Description: Telemetry function that outputs the state of the pool
     */
    public void printCurrState() {
        System.out.println(" State | Dai: " + this.dai + ", Eth: " + this.eth + ", LP: " + this.outstandingLPtokens);
    }
    
    
    
    
    
    
}

