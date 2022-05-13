package StockExchange;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainWindow extends JFrame implements ActionListener {
    private static final int WIDTH = 1500;
    private static final int HEIGHT = 500;
    private static final int LABEL_HEIGHT = 40;
    private static final int LABEL_WIDTH = 300;
    private static final int TEXT_HEIGHT = 40;
    private static final int TEXT_WIDTH = 250;
    private static final int TEXT_X_AND_Y = 100;
    private static final int NEW_USER_BUTTON_X = 360;
    private static final int NEW_USER_BUTTON_Y = 100;

    volatile ListOfAssets assets;
    JButton newUserButton;
    JTextField text;
    ArrayList<UserWindow> users = new ArrayList<>();
    ArrayList<JLabel> labels = new ArrayList<>();
    volatile ArrayList<Order> orderList = new ArrayList<Order>();

    MainWindow(ListOfAssets assetsFromMain){
        assets = assetsFromMain;
        styleOfWindow();
        createLabel();
        createTextField();
        createNewUserButton();
        setVisible(true);
    }
    public void styleOfWindow(){
        setTitle("Stock Exchange");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(0, 255, 145));
        setLocationRelativeTo(null);
        setLayout(null);
    }
    public void createLabel(){
        for(int i = 0; i < assets.sharesList.size(); i++) {
            JLabel label;
            label = new JLabel();
            label.setVisible(false);
            label.setText(assets.sharesList.get(i).name + ": " + assets.sharesList.get(i).prize + "zł   shares: " + assets.sharesList.get(i).numberOfShares);
            label.setFont(new Font("Arial", Font.PLAIN, 15));
            label.setForeground(new Color(158, 140, 140));
            label.setBounds(1000, (1 + i) * LABEL_HEIGHT, LABEL_WIDTH, LABEL_HEIGHT);
            label.setBorder(BorderFactory.createLineBorder(new Color(13, 13, 123), 4));
            label.setVisible(true);
            labels.add(label);
            add(label);
        }
    }
    public void updateLabels(){
        for(int i = 0; i < labels.size(); i++) {
            labels.get(i).setText(assets.sharesList.get(i).name + ": " + assets.sharesList.get(i).prize + "zł   shares: " + assets.sharesList.get(i).numberOfShares);
        }
    }
    public void createTextField(){
        text = new JTextField();
        text.setBounds(TEXT_X_AND_Y, TEXT_X_AND_Y, TEXT_WIDTH, TEXT_HEIGHT);
        add(text);
    }
    public void createNewUserButton(){
        newUserButton = new JButton("New User");
        newUserButton.setBounds(NEW_USER_BUTTON_X, NEW_USER_BUTTON_Y, TEXT_WIDTH, TEXT_HEIGHT);
        newUserButton.addActionListener(this);
        add(newUserButton);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
    if(e.getSource()==newUserButton){
        String username = text.getText();
        text.setText("");
        UserWindow user = new UserWindow(username, assets, orderList);
        users.add(user);
        Thread thread = new Thread(user);
        thread.start();
    }
    }
}
