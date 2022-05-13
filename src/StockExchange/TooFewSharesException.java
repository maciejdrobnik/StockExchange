package StockExchange;

public class TooFewSharesException extends Exception {
    public TooFewSharesException(String message){
        super(message);
    }
}
