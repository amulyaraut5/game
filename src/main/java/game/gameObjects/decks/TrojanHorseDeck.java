package game.gameObjects.decks;

import game.gameObjects.cards.Card;
import game.gameObjects.cards.TrojanHorseCard;

import java.util.ArrayList;

import static utilities.Utilities.*;

/**
 * @author annika
 */
public class TrojanHorseDeck extends DamageCardDeck{

    ArrayList<Card> trojanHorseDeck;

    /**
     * Creates the deck of Trojan Horse cards.
     */
    @Override
    public void createDeck() {
        this.trojanHorseDeck = new ArrayList<>();

        for (int i = 0; i < TROJANHORSE_CARDCOUNT; i++) {
            trojanHorseDeck.add(new TrojanHorseCard());
        }

    }

    @Override
    public ArrayList<Card> getDeck() {
        return trojanHorseDeck;
    }
}
