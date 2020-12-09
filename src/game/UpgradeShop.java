package game;

import game.gameObjects.cards.Card;

import java.util.ArrayList;

public class UpgradeShop {

    private Card[] upgradeShop;
    private ArrayList<Card> upgradeDeck = new ArrayList<Card>();
    private ArrayList<Card> discardedDeck = new ArrayList<Card>();
    private int upgradeShopFill = 0;
    private int emptySlots = 0;


    public UpgradeShop() {

    }

    public void shuffle() {

    }
    
    public void clear() {
        
    }
    
    public void refill() {
        
    }
    
    public void getUpgradeShopFill() {
        for (int i=0; i<upgradeShop.length; i++) {
            if (!(upgradeShop[i] == null) ) {
                upgradeShopFill++;
            }

        }
    }

    public void getEmptySlots() {

    }

}