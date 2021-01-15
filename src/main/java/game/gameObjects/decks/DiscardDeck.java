package game.gameObjects.decks;

import game.gameObjects.cards.Card;

import java.util.ArrayList;

/**
 * @author annika
 */
public class DiscardDeck extends Deck {

    private ArrayList<Card> discardDeck;

    public DiscardDeck() {
        createDeck();
    }

    /**
     * Creates an empty DiscardDeck.
     */
    @Override
    public void createDeck() {
        this.discardDeck = new ArrayList<>();
    }

    @Override
    public ArrayList<Card> getDeck() {
        return discardDeck;
    }

    /**
     * This method is needed, if the ProgrammingDeck gets to small. (less than 9 cards)
     * It shuffles the programming discard pile and replenishes the programming deck.
     */
    public void refillProgrammingDeck(ProgrammingDeck programmingDeck) {
        shuffle();
        programmingDeck.getDeck().addAll(discardDeck);
        discardDeck.clear();
    }
}
