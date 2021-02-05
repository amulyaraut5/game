package game.gameObjects.decks;

import game.gameObjects.cards.Card;
import game.gameObjects.cards.damage.Worm;

import java.util.ArrayList;

import static utilities.Constants.WORM_CARDCOUNT;

/**
 * @author annika
 */
public class WormDeck extends DamageCardDeck {

    private ArrayList<Card> wormDeck;

    public WormDeck() {
        createDeck();
    }

    /**
     * Creates the deck of Worm cards.
     */
    @Override
    public void createDeck() {
        wormDeck = new ArrayList<>();

        for (int i = 0; i < WORM_CARDCOUNT; i++) {
            wormDeck.add(new Worm());
        }
    }

    @Override
    public ArrayList<Card> getDeck() {
        return wormDeck;
    }
}
