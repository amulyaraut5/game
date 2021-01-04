package game;

import game.gameObjects.cards.Card;
import game.gameObjects.cards.UpgradeDeck;

/**
 * This class creates the Upgrade shop where upgrades can be purchased.
 */

public class UpgradeShop {

    /**number of available slots in the UpgradeShop. (=player count)*/
    private Card[] upgradeShop;
    private UpgradeDeck drawDeck;
    private UpgradeDeck discardedDeck;
    private int emptySlots = 0;


    public UpgradeShop() {
    }

    public void shuffle() {

    }

    /**
     * clears the upgrade shop
     */
    public void clear() {
        
    }

    /**
     * Refills the empty slots in the Upgrade Shop
     */
    public void refill() {
        
    }

    public void getEmptySlots() {

    }

}