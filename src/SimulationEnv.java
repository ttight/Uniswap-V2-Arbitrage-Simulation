public class SimulationEnv {
    /*
     * This class is meant to simulate a basic pool environment, and output the status of each pool in the console
     * It is obviously very barebones, but is meant as a way to quickly check the outputs of this model in an example situation.
     */
    
    public static void main(String[] args) {
        poolStruct pool1 = new poolStruct(0, 0, 0.003);
        poolStruct pool2 = new poolStruct(0, 0, 0.003);
        Trader t = new Trader(pool1, pool2);
        
        System.out.println("BEGINNING SIM");
        System.out.println();
        
        double LPpool1 = pool1.add(200, 50);
        double LPpool2 = pool2.add(200, 50);
        printPools(pool1, pool2);
        System.out.println("\nPerson trades 10 Dai in Pool 1 \n");
        pool1.tradeDai(10);
        printPools(pool1, pool2);
        t.scanRecentMove(1, true);
        System.out.println("\nPerson trades 2 Eth in Pool 2 \n");
        pool2.tradeEth(2);
        printPools(pool1, pool2);
        t.scanRecentMove(1, true);
        System.out.println("\nPerson trades 1 Eth in Pool 2 \n ");

        pool2.tradeEth(1);
        System.out.println("\nPerson trades 4 Dai in Pool 2 (Before the trader can execute a trade)");
        pool2.tradeDai(4);
        printPools(pool1, pool2);
        t.scanRecentMove(1, true);
        System.out.println("Trader looks for an arbitrage opportunity... None exists.");
        System.out.println("");
        System.out.println("Person trades 4 Dai in Pool 2");
        System.out.println("");
        pool2.tradeDai(4);
        t.scanRecentMove(1, true);
        
        System.out.println("\nEND ALL TRADES \n");
        System.out.println("Trader has made a total profit of: " + t.totalProfit + " Eth");
        double[] d_e_pool1 = pool1.remove(LPpool1);
        double[] d_e_pool2 = pool2.remove(LPpool2);
        System.out.println("\nThe person that cashes out their liquidity in pool 1 began wtih 200 Dai and 50 Eth, ends with " + d_e_pool1[0] + " Dai, and " + d_e_pool1[1] + " Eth ");
        System.out.println("\nThe person that cashes out their liquidity in pool 2 began wtih 200 Dai and 50 Eth, ends with " + d_e_pool2[0] + " Dai, and " + d_e_pool2[1] + " Eth \n");
        
    }
    
   
    public static void printPools(poolStruct pool1, poolStruct pool2) {
        System.out.println();
        System.out.print("Pool 1: ");
        pool1.printCurrState();
        System.out.print("Pool 2: ");
        pool2.printCurrState();
        System.out.println();
    }
    
}
