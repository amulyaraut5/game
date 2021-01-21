package game.gameObjects.decks;

import game.Game;
import game.Player;
import game.gameObjects.cards.Card;
import game.gameObjects.cards.damage.Spam;
import utilities.JSONProtocol.body.DrawDamage;
import utilities.enums.CardType;

import java.util.ArrayList;

import static utilities.Utilities.SPAM_CARDCOUNT;

/**
 * @author annika
 */
public class SpamDeck extends DamageCardDeck {

    private ArrayList<Card> spamDeck;

    /**
     * Creates the deck of Spam cards.
     */
    @Override
    public void createDeck() {
        this.spamDeck = new ArrayList<>();

        for (int i = 0; i < SPAM_CARDCOUNT; i++) {
            spamDeck.add(new Spam());
        }
    }

    public void drawTwoSpam(Player player){
        ArrayList<Card> twoSpam = Game.getInstance().getSpamDeck().drawCards(2);
        player.getDiscardedProgrammingDeck().getDeck().addAll(twoSpam);

        ArrayList<CardType> drawnSpam = new ArrayList<>();
        drawnSpam.add(CardType.Spam);
        drawnSpam.add(CardType.Spam);
        server.communicateAll(new DrawDamage(player.getID(), drawnSpam));
    }


    @Override
    public void handleEmptyDeck(Player player) {

    }

    @Override
    public ArrayList<Card> getDeck() {
        return spamDeck;
    }
}
