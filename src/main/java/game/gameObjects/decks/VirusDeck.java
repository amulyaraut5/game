package game.gameObjects.decks;

import game.gameObjects.cards.Card;
import game.gameObjects.cards.damage.Virus;

import java.util.ArrayList;

import static utilities.Constants.VIRUS_CARDCOUNT;

/**
 * @author annika
 */
public class VirusDeck extends DamageCardDeck {

    private ArrayList<Card> virusDeck;

    public VirusDeck() {
        createDeck();
    }

    /**
     * Creates the deck of Virus cards.
     */
    @Override
    public void createDeck() {
        virusDeck = new ArrayList<>();

        for (int i = 0; i < VIRUS_CARDCOUNT; i++) {
            virusDeck.add(new Virus());
        }
    }

    @Override
    public ArrayList<Card> getDeck() {
        return virusDeck;
    }
}
