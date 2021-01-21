package game.gameObjects.decks;

import game.Player;
import game.gameObjects.cards.Card;
import game.gameObjects.cards.damage.Virus;

import java.util.ArrayList;

import static utilities.Utilities.VIRUS_CARDCOUNT;

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
            virusDeck.add(new Virus());
        }
    }

    @Override
    public void handleEmptyDeck(Player player) {

    }

    @Override
    public ArrayList<Card> getDeck() {
        return virusDeck;
    }
}
