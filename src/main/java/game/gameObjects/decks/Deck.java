package game.gameObjects.decks;

import game.gameObjects.cards.Card;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author annika
 */
public abstract class Deck {

    private ArrayList<Card> deck;

    /**
     * Creates a suitable deck with the respective cards,
     * has to be implemented in each deck class.
     */
    public abstract void createDeck();

    /**
     * Shuffles the deck.
     */
    public void shuffle() {
        Collections.shuffle(this.getDeck());
    }

    /**
     * Adds a card back to the deck.
     * @param card card to be added back to the deck.
     */
    public void addCard(Card card) {
        deck.add(card);
    }

    /**
     * Checks whether the respective deck is empty.
     * @return
     */
    public boolean isEmpty() {
        if(deck.size() == 0){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Indicates the size of the deck.
     * Depending on the size, e.g., it needs to be reshuffled.
     */
    public int size() {
        return deck.size();
    }

    /**
     * removes first card of the deck
     * @return the removed card
     */
    public Card pop() {
        Card poped = deck.get(0);
        deck.remove(poped);
        return poped;
    }


    /**
     * Return the desired amount of cards and then removes them from the deck.
     */
    public ArrayList<Card> drawCards(int amount) {
        ArrayList<Card> tempDeck = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            tempDeck.add(deck.get(i));
            deck.remove(i);
        }
        return tempDeck;
    }

    /**
     *  Returns the deck.
     */
    public abstract ArrayList<Card> getDeck();
}