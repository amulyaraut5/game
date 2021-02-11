package game.gameObjects.decks;

import game.gameObjects.cards.Card;
import game.gameObjects.cards.damage.Trojan;

import java.util.ArrayList;

import static utilities.Constants.TROJAN_CARDCOUNT;

/**
 * @author annika
 */
public class TrojanDeck extends Deck {

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
