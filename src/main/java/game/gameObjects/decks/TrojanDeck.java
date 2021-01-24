package game.gameObjects.decks;

import game.Player;
import game.gameObjects.cards.Card;
import game.gameObjects.cards.damage.Trojan;

import java.util.ArrayList;

import static utilities.Utilities.TROJANHORSE_CARDCOUNT;

/**
 * @author annika
 */
public class TrojanDeck extends DamageCardDeck {

    private ArrayList<Card> trojanDeck;

    public TrojanDeck(){
        createDeck();
    }

    /**
     * Creates the deck of Trojan cards.
     */
    @Override
    public void createDeck() {
        this.trojanDeck = new ArrayList<>();

        for (int i = 0; i < TROJANHORSE_CARDCOUNT; i++) {
            trojanDeck.add(new Trojan());
        }

    }

    @Override
    public void handleEmptyDeck(Player player) {

    }

    @Override
    public ArrayList<Card> getDeck() {
        return trojanDeck;
    }
}
