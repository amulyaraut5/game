package game.gameObjects.decks;

import game.Player;
import game.gameObjects.cards.Card;
import game.gameObjects.cards.damage.Spam;

import java.util.ArrayList;

import static utilities.Constants.SPAM_CARDCOUNT;

/**
 * @author annika
 */
public class SpamDeck extends DamageCardDeck {

    private ArrayList<Card> spamDeck;

    public SpamDeck() {
        createDeck();
    }

    /**
     * Creates the deck of Spam cards.
     */
    @Override
    public void createDeck() {
        spamDeck = new ArrayList<>();

        for (int i = 0; i < SPAM_CARDCOUNT; i++) {
            spamDeck.add(new Spam());
        }
    }

    @Override
    public void handleEmptyDeck(Player player) {

    }

    @Override
    public ArrayList<Card> getDeck() {
        return spamDeck;
    }
}
