package game.gameObjects.decks;

import game.gameObjects.cards.Card;

import java.util.ArrayList;

/**
 * @author annika
 */
public class VirusDeck extends DamageCardDeck {

    ArrayList<Card> virusDeck;

    @Override
    public void createDeck() {

    }

    @Override
    public ArrayList<Card> getDeck() {
        return virusDeck;
    }
}
