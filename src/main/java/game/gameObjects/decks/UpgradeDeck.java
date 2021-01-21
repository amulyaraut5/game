package game.gameObjects.decks;

import game.Player;
import game.gameObjects.cards.Card;

import java.util.ArrayList;

/**
 * Upgrade Deck including all temporary and permanent upgrade cards
 */
public class UpgradeDeck extends Deck {

    private ArrayList<Card> upgradeDeck;

    @Override
    public void createDeck() {

    }

    @Override
    public void handleEmptyDeck(Player player) {

    }

    @Override
    public ArrayList<Card> getDeck() {
        return upgradeDeck;
    }
}
