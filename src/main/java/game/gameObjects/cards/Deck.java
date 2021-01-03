package game.gameObjects.cards;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Deck {

    private ArrayList<Card> deck;

    /**
     * Shuffles the deck.
     */
    public void shuffle() {
        Collections.shuffle(deck);
    }

    /**
     * Adds a card back to the deck.
     * @param card card to be added back to the deck.
     */
    public void addCard(Card card) {
        deck.add(card);
    }

    public void drawCard(){
    }

    /**
     * removes first card of the deck
     *
     * @return poped the removed card
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
}
