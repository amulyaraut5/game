package game.gameObjects.decks;

import game.gameObjects.cards.Card;
import game.gameObjects.cards.damage.Trojan;

import java.util.ArrayList;

import static utilities.Utilities.TROJANHORSE_CARDCOUNT;

/**
 * @author annika
 */
public class TrojanDeck extends DamageCardDeck {

    ArrayList<Card> trojanDeck;

    /**
     * Creates the deck of Trojan cards.
     */
    @Override
    public void createDeck() {
        this.trojanDeck = new ArrayList<>();

        for (int i = 0; i < TROJANHORSE_CARDCOUNT; i++) {
            trojanDeck.add(new Trojan());
        }

    }

    @Override
    public ArrayList<Card> getDeck() {
        return trojanDeck;
    }
}