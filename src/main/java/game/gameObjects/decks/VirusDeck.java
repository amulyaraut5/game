package game.gameObjects.decks;

import game.gameObjects.cards.Card;
import game.gameObjects.cards.VirusCard;

import java.util.ArrayList;

import static utilities.Utilities.*;

/**
 * @author annika
 */
public class VirusDeck extends DamageCardDeck {

    ArrayList<Card> virusDeck;

    /**
     * Creates the deck of Virus cards.
     */
    @Override
    public void createDeck() {
        this.virusDeck = new ArrayList<>();

        for (int i = 0; i < VIRUS_CARDCOUNT; i++) {
            virusDeck.add(new VirusCard());
        }
    }

    @Override
    public ArrayList<Card> getDeck() {
        return virusDeck;
    }
}
