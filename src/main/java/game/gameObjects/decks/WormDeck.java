package game.gameObjects.decks;

import game.gameObjects.cards.Card;
import game.gameObjects.cards.damage.Wurm;

import java.util.ArrayList;

import static utilities.Utilities.WORM_CARDCOUNT;

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
            wormDeck.add(new Wurm());
        }
    }

    @Override
    public ArrayList<Card> getDeck() {
        return wormDeck;
    }
}
