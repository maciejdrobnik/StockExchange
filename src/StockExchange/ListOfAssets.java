package StockExchange;


import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ListOfAssets {
    ArrayList<Asset> sharesList = new ArrayList();
    String[] sharesNames = {"BMW", "Audi", "Mercedes", "Ferrari", "Porsche", "Lamborghini", "Pontiac", "Kruk", "CCC", "TZMO",
            "Dino", "CD Project", "Activision", "Blizzard", "Riot games"};
    public ListOfAssets(){
        createShares();
        this.sharesList = chooseShares();
    }
    public void createShares(){
        for(int i = 0; i < sharesNames.length; i++)
        {
            sharesList.add(new Asset(sharesNames[i]));
        }
    }
    ArrayList<Asset> chooseShares() {
       ArrayList<Asset> chosenShares = new ArrayList<>();
        int numberOfChosenAssets = ThreadLocalRandom.current().nextInt(5, 11);
        int top = 0;
        boolean chosen = false;
        while (!chosen) {
            boolean isAdded = false;
            int temp = (int) Math.floor(Math.random() * (sharesNames.length));
            for(int i = 0; i < chosenShares.size(); i ++)
            {
                if(sharesList.get(temp).equals(chosenShares.get(i)))
                {
                    isAdded = true;
                }
            }
            if(!isAdded)
            {
                chosenShares.add(sharesList.get(temp));
                top++;
            }
            if(top == numberOfChosenAssets)
            {
                chosen = true;
            }

        }
        return chosenShares;
    }
    public void changeAllPrizes() throws WrongSharePriceException
    {

            for (int i = 0; i < sharesList.size(); i++) {
                try {
                    sharesList.get(i).changePrize();
                } catch (WrongSharePriceException e) {
                    System.out.println("Sth wrong happened with price");
                }
            }
    }
}
