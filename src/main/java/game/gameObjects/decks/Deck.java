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
     * Checks whether the respective deck is empty.
     *
     * @return
     */
    public boolean isEmpty() {
        return getDeck().isEmpty();
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
        //if(this.getDeck().size() >= amount) {
        ArrayList<Card> currentDeck = getDeck();

        ArrayList<Card> tempDeck = new ArrayList<>();
        //logger.info("drawCards PROG-DECK: " +this.getDeck());
        for (int i = 0; i < amount; i++) {
            //logger.info("draw Cards - i: " + i);
            tempDeck.add(currentDeck.get(i));
            //logger.info("drawCards OUTPUT : " + tempDeck);
        }
        getDeck().removeAll(tempDeck);
        //logger.info("CurrentDeck: " +currentDeck);
        //logger.info("drawCards REMOVE progdeck : " + this.getDeck());
        //logger.info("drawCards RETURN : " + tempDeck);
        return tempDeck;
            /*
        } else {
            ArrayList<Card> tempDeck = new ArrayList<>();
            for (int i = 0; i < this.getDeck().size(); i++) {
                tempDeck.add(this.getDeck().get(i));
                this.getDeck().remove(i);
            }
            this.handleEmptyDeck(player);
            for (int i = 0; i < amount - tempDeck.size(); i++) {
                tempDeck.add(this.getDeck().get(i));
                this.getDeck().remove(i);
            }
            return tempDeck;
        }

             */
    }

    /**
     * Returns the deck.
     */
    public abstract ArrayList<Card> getDeck();
}
