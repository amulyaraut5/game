package game.gameObjects.decks;

import game.gameObjects.cards.Card;
import server.Server;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The abstract class Deck provides methods for the different inheriting  decks.
 * @author annika
 */

public abstract class Deck {
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
     * Adds a card to the deck.
     *
     * @param card card to be added.
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
     * Takes the first card of the deck and removes it
     *
     * @return the removed card
     */
    public Card pop() {
        Card popped = getDeck().get(0);
        getDeck().remove(popped);
        return popped;
    }

    /**
     * Takes the desired card from the deck and removes it
     * @param card the card to be removed
     * @return the desired card
     */
    public Card popThisCard(Card card) {
        int cardIndex = getDeck().indexOf(card);
        Card popped = getDeck().get(cardIndex);
        getDeck().remove(popped);
        return popped;
    }

    /**
     * Return the desired amount of cards and then removes them from the deck.
     *
     * @param amount the desired amount of cards
     * @return the desired amount of cards
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
