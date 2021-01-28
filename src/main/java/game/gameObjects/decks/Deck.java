package game.gameObjects.decks;

import game.Player;
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
        Collections.shuffle(this.getDeck());
    }

    /**
     * Adds a card back to the deck.
     *
     * @param card card to be added back to the deck.
     */
    public void addCard(Card card) {
        this.getDeck().add(card);
    }

    /**
     * Checks whether the respective deck is empty.
     *
     * @return
     */
    public boolean isEmpty() {
        return this.getDeck().isEmpty();
    }

    /**
     * Indicates the size of the deck.
     * Depending on the size, e.g., it needs to be reshuffled.
     */
    public int size() {
        return this.getDeck().size();
    }

    /**
     * removes first card of the deck
     *
     * @return the removed card
     */
    public Card pop() {
        Card popped = this.getDeck().get(0);
        this.getDeck().remove(popped);
        return popped;
    }

    /**
     * Return the desired amount of cards and then removes them from the deck.
     */
    public ArrayList<Card> drawCards(int amount) {
        //if(this.getDeck().size() >= amount) {
            ArrayList<Card> tempDeck = new ArrayList<>();
            for (int i = 0; i < amount; i++) {
                //logger.info("draw Cards - i: " + i);
                //logger.info("drawCards PROG-DECK: " +this.getDeck());
                tempDeck.add(this.getDeck().get(i));
                //logger.info("drawCards OUTPUT : " + tempDeck);
            }
            for (int i = 0; i < amount; i++) {
                //this.getDeck().remove(i);
                //logger.info("drawCards REMOVE progdeck : " + this.getDeck());
            }
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

    public abstract void handleEmptyDeck(Player player);

    /**
     * Returns the deck.
     */
    public abstract ArrayList<Card> getDeck();
}
