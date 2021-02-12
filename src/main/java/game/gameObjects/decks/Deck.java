package game.gameObjects.decks;

import game.gameObjects.cards.Card;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author annika
 */

public abstract class Deck {
    private static final Logger logger = LogManager.getLogger();
    protected Server server = Server.getInstance();

    /**
     * Creates a suitable deck with the respective cards,
     * has to be implemented in each deck class.
     */
    public abstract void createDeck();

    /**
     * Shuffles the deck.
     */
    public void shuffle() {
        Collections.shuffle(getDeck());
    }

    /**
     * Adds a card back to the deck.
     *
     * @param card card to be added back to the deck.
     */
    public void addCard(Card card) {
        getDeck().add(card);
    }

    /**
     * Indicates the size of the deck.
     * Depending on the size, e.g., it needs to be reshuffled.
     */
    public int size() {
        return getDeck().size();
    }

    /**
     * removes first card of the deck
     *
     * @return the removed card
     */
    public Card pop() {
        Card popped = getDeck().get(0);
        getDeck().remove(popped);
        return popped;
    }

    public Card popThisCard(Card card) {
        int cardIndex = getDeck().indexOf(card);
        Card popped = getDeck().get(cardIndex);
        getDeck().remove(popped);
        return popped;
    }

    /**
     * Return the desired amount of cards and then removes them from the deck.
     */
    public ArrayList<Card> drawCards(int amount) {
        ArrayList<Card> currentDeck = getDeck();

        ArrayList<Card> tempDeck = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            tempDeck.add(currentDeck.get(i));
        }
        getDeck().removeAll(tempDeck);
        return tempDeck;
    }

    /**
     * Checks whether the respective deck is empty.
     *
     * @return true if deck is empty
     */
    public boolean isEmpty() {
        return getDeck().isEmpty();
    }

    /**
     * Returns the deck.
     */
    public abstract ArrayList<Card> getDeck();
}
