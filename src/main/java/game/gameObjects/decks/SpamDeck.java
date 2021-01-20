package game.gameObjects.decks;

import game.gameObjects.cards.Card;
import game.gameObjects.cards.damage.Spam;

import java.util.ArrayList;

import static utilities.Utilities.SPAM_CARDCOUNT;

/**
 * @author annika
 */
public class SpamDeck extends DamageCardDeck {

    private ArrayList<Card> spamDeck;


    /**
     * Creates the deck of Spam cards.
     */
    @Override
    public void createDeck() {
        this.spamDeck = new ArrayList<>();

        for (int i = 0; i < SPAM_CARDCOUNT; i++) {
            spamDeck.add(new Spam());
        }
    }

    @Override
    public ArrayList<Card> getDeck() {
        return spamDeck;
    }
}
