package StockExchange;


import java.util.*;



public class StockExchange {
    private static final ListOfAssets shares = new ListOfAssets();
    private static MainWindow frame;
    private static final int DELAY = 1000;
    private static final int PERIOD = 5000;

    public static void main(String[] args) {
        frame = new MainWindow(shares);
        createTimerTask();

    }
    public static void createTimerTask(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask(){
            @Override
            public void run() {
                try {
                    shares.changeAllPrizes();
                } catch (WrongSharePriceException e) {
                    e.printStackTrace();
                }
                frame.updateLabels();
                try {
                    checkOrders();
                }
                catch(WrongInputOfUser e){
                    System.out.println("Wrong input");
                }


            }
        };
        timer.schedule(task, DELAY, PERIOD);
    }
    public static void checkOrders() throws WrongInputOfUser {
        for(int i = 0; i < frame.orderList.size(); i++){
            checkOrder(frame.orderList.get(i), i);
        }
    }
    public static void checkOrder(Order order, int number) {
        if(order.type == "buy"){
            if(shares.sharesList.get(order.index).prize <= order.price ){
                int index = findUser(order.user);
                frame.users.get(index).buy(order.numberOfShare, shares.sharesList.get(order.index).name, order.price);
                frame.orderList.remove(number);
            }
        }
        else{
            if(shares.sharesList.get(order.index).prize >= order.price){
                int index = findUser(order.user);
                frame.users.get(index).sell(order.numberOfShare, shares.sharesList.get(order.index).name, order.price);
                frame.orderList.remove(number);
            }
        }
    }
    public static int findUser(String name)
    {
        for(int i = 0; i < frame.users.size(); i++){
            if(name == frame.users.get(i).userName){
                return i;
            }
        }
        return 0;
    }
}
