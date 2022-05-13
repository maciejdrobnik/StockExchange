package StockExchange;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;

public class UserWindow extends JFrame implements ActionListener, Runnable {
    private final int MONEY_TO_SPEND = 10000;
    private final int SQUARE_SIZE = 500;
    private final int ROWS = 5;
    private final int COLUMNS = 2;
    private final int TEXTFIELDS = 4;
    ListOfAssets assets;
    String userName;
    float moneyToSpend;
    ArrayList<Asset> ownedShares = new ArrayList<>();
    String[] text = {"User data:", "Asset name: ", "Number of shares: ", "Price:"};
    JButton sell;
    JButton buy;
    JLabel dataLabel;
    ArrayList<JLabel> labels = new ArrayList();
    ArrayList<JTextField> textFields = new ArrayList();
    ArrayList<Order> orders = new ArrayList<Order>();;



    public UserWindow(String name, ListOfAssets assets, ArrayList<Order> orders) {
        this.userName = name;
        this.moneyToSpend = MONEY_TO_SPEND;
        this.assets = assets;
        this.orders = orders;
        styleOfWindow();
        createGrid();
        createBuyButton();
        createSellButton();
        setVisible(true);
    }

    public void styleOfWindow() {
        setTitle("User: " + this.userName);
        setSize(SQUARE_SIZE, SQUARE_SIZE);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(0, 255, 145));
        setLocationRelativeTo(null);
        setLayout(new GridLayout(ROWS, COLUMNS));
    }

    public void createLabel(int number) {
        JLabel label;
        label = new JLabel();
        label.setVisible(false);
        label.setText(text[number]);
        label.setFont(new Font("Arial", Font.PLAIN, 15));
        label.setForeground(new Color(158, 140, 140));
        label.setBorder(BorderFactory.createLineBorder(new Color(13, 13, 123), 4));
        label.setVisible(true);
        labels.add(label);
        add(label);
    }

    public void createTextField() {
        JTextField text = new JTextField();
        add(text);
        textFields.add(text);
    }

    public void createGrid() {
        createLabel(0);
        createData();
        for (int i = 1; i < TEXTFIELDS; i++) {
            createLabel(i);
            createTextField();
        }
    }

    public void createData() {
        dataLabel = new JLabel();
        setTextOfData();
        add(dataLabel);
    }
    public void setTextOfData(){
        String text1 = "<html>Username: " + this.userName ;
        String text2 = "<br>Money to spend: " + this.moneyToSpend;
        String text3 = "";

        for (int i = 0; i < ownedShares.size(); i++) {
            text3 += ("<br>" + ownedShares.get(i).name + ": " + ownedShares.get(i).numberOfShares);
        }
        text3 += "</html>";
        dataLabel.setText(text1 + text2 + text3);
    }

    public void createBuyButton() {
        buy = new JButton("Buy");
        buy.addActionListener(this);
        add(buy);
    }

    public void createSellButton() {
        sell = new JButton("Sell");
        sell.addActionListener(this);
        add(sell);
    }
    public void addBuyOrder(int numberOfShares, int index, float price, String name) throws TooFewSharesException, TooLittleMoney {
        float totalCost = price * numberOfShares;
        if(totalCost > moneyToSpend){
            clearTextFields();
            throw new TooLittleMoney("To little money");
        }
        if(assets.sharesList.get(index).numberOfShares < numberOfShares){
            clearTextFields();
            throw new TooFewSharesException("Too few shares");
        }
        Order order = new Order("buy", numberOfShares, index, price, name);
        orders.add(order);
    }
    public void addSellOrder(int numberOfShares, int index, float price, String name) throws TooFewSharesException {
        int indexUser = 0;
        try {
            indexUser = findUserAsset(name);
        }
        catch(WrongInputOfUser e){
            System.out.println("");
        }
        if(ownedShares.get(indexUser).numberOfShares < numberOfShares){
            clearTextFields();
            throw new TooFewSharesException("Too few shares");
        }
        Order order = new Order("sell", numberOfShares, index, price, name);
        orders.add(order);
    }

    public void buy(int number, String nameOfAsset, float price)  {
        try{
            float totalcost = number * price;
            int index = findAsset(nameOfAsset);
            assets.sharesList.get(index).numberOfShares -= number;
            boolean found = false;
            int indexUserShares = 0;
            for (int i = 0; i < ownedShares.size(); i++) {
                if (Objects.equals(nameOfAsset, ownedShares.get(i).name)) {
                    found = true;
                    indexUserShares = i;
                }
            }
            if (found) {
                ownedShares.get(indexUserShares).numberOfShares += number;
            } else {
                ownedShares.add(new Asset(nameOfAsset, number));
            }
            moneyToSpend -= totalcost;
            setTextOfData();
        }
        catch(WrongInputOfUser e){
            clearTextFields();
            System.out.println("Wrong input, try again");
        }
    }
    public void sell(int number, String nameOfAsset, float price){
        try {
            float totalCost = price * number;
            int indexUser = findUserAsset(nameOfAsset);
            int indexAll = findAsset(nameOfAsset);
            if(ownedShares.get(indexUser).numberOfShares < number){
                clearTextFields();
                throw new TooFewSharesException("Too few shares");
            }
            ownedShares.get(indexUser).numberOfShares -= number;
            assets.sharesList.get(indexAll).numberOfShares += number;
            if(ownedShares.get(indexUser).numberOfShares == 0){
                ownedShares.remove(indexUser);
            }
            moneyToSpend += totalCost;
        }
        catch(WrongInputOfUser e){
            clearTextFields();
            System.out.println("Wrong input in selling");
        }
        catch(TooFewSharesException e1)
        {
            clearTextFields();
            System.out.println("Too few shares");
        }
        setTextOfData();

}
    public int findAsset(String nameOfAsset) throws WrongInputOfUser{
        boolean found = false;
        int chosenAsset = 0;
        for(int i = 0; i < assets.sharesList.size(); i++){
            if(Objects.equals(nameOfAsset, assets.sharesList.get(i).name)){
                chosenAsset = i;
                found = true;
            }
        }
        if(found){
            return chosenAsset;
        }
        else{
            clearTextFields();
            throw new WrongInputOfUser("Wrong input of user");
        }
    }
    public int findUserAsset(String nameOfAsset) throws WrongInputOfUser{
        boolean foundUser = false;
        int chosenAssetUser = 0;

        for(int i = 0; i < ownedShares.size(); i++){
            if(Objects.equals(nameOfAsset, ownedShares.get(i).name)){
                chosenAssetUser = i;
                foundUser = true;
            }
        }
        if(foundUser){
            return chosenAssetUser;
        }
        else{
            clearTextFields();
            throw new WrongInputOfUser("Wrong input of user");
        }
    }

        public void clearTextFields(){
            for(int i = 0; i < textFields.size(); i++){
                textFields.get(i).setText("");
            }
        }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buy) {
            try {
                addBuyOrder(Integer.parseInt(textFields.get(1).getText()), findAsset(textFields.get(0).getText()),
                        Float.parseFloat(textFields.get(2).getText()), this.userName);
                setTextOfData();
                clearTextFields();
            }
            catch (WrongInputOfUser e2){
                clearTextFields();
                System.out.println("Wrong Input");
            }
            catch(TooFewSharesException e3){
                clearTextFields();
                System.out.println("Too few shares");
            }
            catch(TooLittleMoney e4){
                clearTextFields();
                System.out.println("Too little money");
            }
            } else if (e.getSource() == sell)
            try {
                addSellOrder(Integer.parseInt(textFields.get(1).getText()), findAsset(textFields.get(0).getText()),
                        Float.parseFloat(textFields.get(2).getText()), this.userName);
                setTextOfData();
                clearTextFields();
            }

            catch (WrongInputOfUser e2){
                clearTextFields();
                System.out.println("");
            }
            catch(TooFewSharesException e3) {
                clearTextFields();
                System.out.println("Too few shares");
            }
    }

    @Override
    public void run() {
        System.out.println("");
    }
}


