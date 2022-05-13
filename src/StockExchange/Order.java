package StockExchange;

public class Order {
    String type;
    int numberOfShare;
    int index;
    float price;
    String user;
    public Order(String type, int numberOfShare, int index, float price, String user){
        this.type = type;
        this.numberOfShare = numberOfShare;
        this.index = index;
        this.price = price;
        this.user = user;
    }
}
