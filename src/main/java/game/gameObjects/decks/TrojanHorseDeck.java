package game.gameObjects.decks;

import game.gameObjects.cards.Card;

import java.util.ArrayList;

/**
 * @author annika
 */
public class TrojanHorseDeck extends DamageCardDeck{

    ArrayList<Card> trojanHorseDeck;

    @Override
    public void createDeck() {

    }

    @Override
    public ArrayList<Card> getDeck() {
        return trojanHorseDeck;
    }
}
