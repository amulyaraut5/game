package game.gameObjects.decks;

import game.gameObjects.cards.Card;
import game.gameObjects.cards.damage.Worm;

import java.util.ArrayList;

import static utilities.Constants.WORM_CARDCOUNT;

/**
 * The WormDeck inherits the methods of the abstract class Deck.
 * It includes a deck containing all worm cards.
 * @author annika
 */
public class WormDeck extends Deck {

    /**
     * ArrayList for the cards in the worm deck.
     */
    private ArrayList<Card> wormDeck;

    public WormDeck() {
        createDeck();
    }

    /**
     * Creates the deck of worm cards.
     */
    @Override
    public void createDeck() {
        wormDeck = new ArrayList<>();

        for (int i = 0; i < WORM_CARDCOUNT; i++) {
            wormDeck.add(new Worm());
        }
    }

    @Override
    public ArrayList<Card> getDeck() {
        return wormDeck;
    }
}
