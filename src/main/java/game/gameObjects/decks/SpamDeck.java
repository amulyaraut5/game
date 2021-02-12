package game.gameObjects.decks;

import game.gameObjects.cards.Card;
import game.gameObjects.cards.damage.Spam;

import java.util.ArrayList;

import static utilities.Constants.SPAM_CARDCOUNT;

/**
 * The SpamDeck inherits the methods of the abstract class Deck.
 * It includes a deck containing all spam cards.
 * @author annika
 */
public class SpamDeck extends Deck {

    /**
     * ArrayList for the cards in the spam deck.
     */
    private ArrayList<Card> spamDeck;

    public SpamDeck() {
        createDeck();
    }

    /**
     * Creates the deck of Spam cards.
     */
    @Override
    public void createDeck() {
        spamDeck = new ArrayList<>();

        for (int i = 0; i < SPAM_CARDCOUNT; i++) {
            spamDeck.add(new Spam());
        }
    }

    @Override
    public ArrayList<Card> getDeck() {
        return spamDeck;
    }
}
