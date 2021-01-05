package game.gameObjects.decks;

import game.gameObjects.cards.Card;

import java.util.ArrayList;

/**
 * @author annika
 */
public class WormDeck extends DamageCardDeck {

    ArrayList<Card> wormDeck;

    @Override
    public void createDeck() {

    }

    @Override
    public ArrayList<Card> getDeck() {
        return wormDeck;
    }
}
