package game.gameObjects.decks;

import game.gameObjects.cards.Card;
import game.gameObjects.cards.WormCard;

import java.util.ArrayList;

import static utilities.Utilities.*;

/**
 * @author annika
 */
public class WormDeck extends DamageCardDeck {

    ArrayList<Card> wormDeck;

    /**
     * Creates the deck of Worm cards.
     */
    @Override
    public void createDeck() {
        this.wormDeck = new ArrayList<>();

        for (int i = 0; i < WORM_CARDCOUNT; i++) {
            wormDeck.add(new WormCard());
        }
    }

    @Override
    public ArrayList<Card> getDeck() {
        return wormDeck;
    }
}
