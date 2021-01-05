package game.gameObjects.decks;

import game.gameObjects.cards.Card;
import game.gameObjects.decks.Deck;

import java.util.ArrayList;

/** Upgrade Deck including all temporary and permanent upgrade cards*/
public class UpgradeDeck extends Deck {

    ArrayList<Card> upgradeDeck;

    @Override
    public void createDeck() {

    }

    @Override
    public ArrayList<Card> getDeck() {
        return upgradeDeck;
    }
}
