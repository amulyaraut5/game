package game.gameObjects.decks;

import game.gameObjects.cards.Card;
import game.gameObjects.cards.SpamCard;

import java.util.ArrayList;

import static utilities.Utilities.*;

/**
 * @author annika
 */
public class SpamDeck extends DamageCardDeck {

    ArrayList<Card> spamDeck;

    @Override
    public void createDeck() {
        this.spamDeck = new ArrayList<>();

        for (int i = 0; i < SPAM_CARDCOUNT; i++) {
            spamDeck.add(new SpamCard());
        }
    }

    @Override
    public ArrayList<Card> getDeck() {
        return spamDeck;
    }
}
