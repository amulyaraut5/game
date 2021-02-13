package game.gameObjects.decks;

import game.gameObjects.cards.Card;
import game.gameObjects.cards.damage.Virus;

import java.util.ArrayList;

import static utilities.Constants.VIRUS_CARDCOUNT;

/**
 * The VirusDeck inherits the methods of the abstract class Deck.
 * It includes a deck containing all virus cards.
 * @author annika
 */
public class VirusDeck extends Deck {

    /**
     * ArrayList for the cards in the virus deck.
     */
    private ArrayList<Card> virusDeck;

    public VirusDeck() {
        createDeck();
    }

    /**
     * Creates the deck of virus cards.
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
