package game.gameObjects.decks;

import game.gameObjects.cards.Card;

import java.util.ArrayList;

/**
 * @author annika
 */
public class SpamDeck extends DamageCardDeck {

    ArrayList<Card> spamDeck;

    @Override
    public void createDeck() {

    }

    @Override
    public ArrayList<Card> getDeck() {
        return spamDeck;
    }
}
