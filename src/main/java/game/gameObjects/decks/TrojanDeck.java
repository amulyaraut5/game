package game.gameObjects.decks;

import game.gameObjects.cards.Card;
import game.gameObjects.cards.damage.Trojan;

import java.util.ArrayList;

import static utilities.Constants.TROJAN_CARDCOUNT;

/**
 * The TrojanDeck inherits the methods of the abstract class Deck.
 * It includes a deck containing all trojan cards.
 * @author annika
 */
public class TrojanDeck extends Deck {

    /**
     * ArrayList for the cards in the trojan deck.
     */
    private ArrayList<Card> trojanDeck;

    public TrojanDeck() {
        createDeck();
    }

    /**
     * Creates the deck of Trojan cards.
     */
    @Override
    public void createDeck() {
        trojanDeck = new ArrayList<>();

        for (int i = 0; i < TROJAN_CARDCOUNT; i++) {
            trojanDeck.add(new Trojan());
        }
    }

    @Override
    public ArrayList<Card> getDeck() {
        return trojanDeck;
    }
}
