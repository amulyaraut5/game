package game.gameObjects.decks;

import game.Player;
import game.gameObjects.cards.Card;
import game.gameObjects.cards.programming.*;
import utilities.JSONProtocol.body.ShuffleCoding;

import java.util.ArrayList;

import static utilities.Utilities.*;

/**
 * @author annika
 */
public class ProgrammingDeck extends Deck {
    private ArrayList<Card> programmingDeck;

    DiscardDeck discardDeck = new DiscardDeck();


    public ProgrammingDeck() {
        createDeck();
        shuffle();
    }

    /**
     * Creates a deck with all 20 programming cards
     * each player has in the Programming Deck at the beginning of the game.
     */
    public void createDeck() {
        programmingDeck = new ArrayList<>();

        // Move 1 Cards
        for (int i = 0; i < MOVE1_CARDCOUNT; i++) {
            addCard(new MoveI());
        }
        // Move 2 Cards
        for (int i = 0; i < MOVE2_CARDCOUNT; i++) {
            addCard(new MoveII());
        }
        // Move3 Card
        for (int i = 0; i < MOVE3_CARDCOUNT; i++) {
            addCard(new MoveIII());
        }

        // Back Up Card
        for (int i = 0; i < BACKUP_CARDCOUNT; i++) {
            addCard(new BackUp());
        }

        // Left Turn Cards
        for (int i = 0; i < TURNLEFT_CARDCOUNT; i++) {
            addCard(new TurnLeft());
        }
        // Right Turn Cards
        for (int i = 0; i < TURNRIGHT_CARDCOUNT; i++) {
            addCard(new TurnRight());
        }
        // U-Turn Card
        for (int i = 0; i < UTURN_CARDCOUNT; i++) {
            addCard(new UTurn());
        }

        // Power Up Cards
        for (int i = 0; i < POWERUP_CARDCOUNT; i++) {
            addCard(new PowerUp());
        }

        // Again Card
        for (int i = 0; i < AGAIN_CARDCOUNT; i++) {
            addCard(new Again());
        }
    }

    @Override
    public void handleEmptyDeck(Player player) {
        //player.getDiscardedProgrammingDeck().refillProgrammingDeck(this);
        //player.message(new ShuffleCoding(player.getID()));
    }

    @Override
    public ArrayList<Card> getDeck() {
        return programmingDeck;
    }
}
