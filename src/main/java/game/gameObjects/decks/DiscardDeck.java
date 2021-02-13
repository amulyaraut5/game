package game.gameObjects.decks;

import game.gameObjects.cards.Card;

import java.util.ArrayList;

/**
 * The DiscardDeck inherits the methods of the abstract class Deck.
 * It includes an empty deck and provides a method that shuffles the discard pile and replenishes the programming deck.
 *
 * @author annika
 */
public class DiscardDeck extends Deck {

    /**
     * ArrayList for the cards in the discard deck.
     */
    private ArrayList<Card> discardDeck;

    public DiscardDeck() {
        createDeck();
    }

    /**
     * Creates an empty DiscardDeck.
     */
    @Override
    public void createDeck() {
        discardDeck = new ArrayList<>();
    }

    /**
     * This method is needed, if the ProgrammingDeck gets to small. (less than 9 cards)
     * It shuffles the discard pile and replenishes the programming deck.
     */
    public void refillProgrammingDeck(ProgrammingDeck programmingDeck) {
        shuffle();
        programmingDeck.getDeck().addAll(discardDeck);
        discardDeck.clear();
    }

    @Override
    public ArrayList<Card> getDeck() {
        return discardDeck;
    }
}
