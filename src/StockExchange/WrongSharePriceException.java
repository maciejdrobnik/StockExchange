package StockExchange;

public class WrongSharePriceException extends Exception{
    public WrongSharePriceException(String message){
        super(message);
    }
}
