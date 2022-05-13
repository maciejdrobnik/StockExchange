package StockExchange;

import java.util.concurrent.ThreadLocalRandom;

public class Asset {
    private static final int MAX_PRICE = 1000;
    private static final int MAX_CHANGE = 3;
    private static final int MAX_NUMBER_OF_SHARES = 1000;
    private static final int MIN_NUMBER_OF_SHARES = 100;
    float prize;
    int numberOfShares;
    String name;
    int factorOfSoldShares;
    public Asset(String name){
        this.prize = ThreadLocalRandom.current().nextFloat(1, MAX_PRICE);
        this.numberOfShares = ThreadLocalRandom.current().nextInt(MIN_NUMBER_OF_SHARES, MAX_NUMBER_OF_SHARES);
        this.name = name;
        this.factorOfSoldShares = 0;
    }
    public Asset(String name, int numberOfShares){
        this.prize = ThreadLocalRandom.current().nextFloat(1, MAX_PRICE);
        this.numberOfShares = numberOfShares;
        this.name = name;
        this.factorOfSoldShares = 0;
    }

    public void changePrizeUp() throws WrongSharePriceException {
        if (this.factorOfSoldShares == 0) {
            this.prize += ThreadLocalRandom.current().nextFloat(1, MAX_CHANGE);
        }
    else{
        this.prize *= ( this.factorOfSoldShares + 1);
        }
        if (this.prize > MAX_PRICE) {
            this.prize = MAX_PRICE;
            throw new WrongSharePriceException("Too high prize");
        }
        this.factorOfSoldShares = 0;
    }
    public void changePrizeDown() throws WrongSharePriceException{
        if(this.factorOfSoldShares == 0) {
            this.prize -= ThreadLocalRandom.current().nextFloat(1, MAX_CHANGE);
        }else{
            this.prize *= (1 - this.factorOfSoldShares);
        }
        if (this.prize < 0) {
            this.prize = 0;
            throw new WrongSharePriceException("Too low prize");
        }
        this.factorOfSoldShares = 0;
    }
    public void changePrize() throws WrongSharePriceException{
        try {
            if(factorOfSoldShares == 0) {
                int check = ThreadLocalRandom.current().nextInt(0, 2);
                if (check == 1) {
                    changePrizeUp();
                } else if(check == 0) {
                    changePrizeDown();
                }
            }
            else{
                if(factorOfSoldShares > 0){
                    changePrizeUp();
                }
                else{
                    changePrizeDown();
                }
            }
        }
        catch(WrongSharePriceException e){
            System.out.println("Sth wrong happened");
        }
    }
}
