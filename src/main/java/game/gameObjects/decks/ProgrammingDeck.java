package game.gameObjects.decks;

import game.gameObjects.cards.*;

import java.util.ArrayList;

import static utilities.Utilities.*;

/**
 * @author annika
 */
public class ProgrammingDeck extends Deck {

    ArrayList<Card> programmingDeck;

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
            addCard(new Move1Card());
        }
        // Move 2 Cards
        for (int i = 0; i < MOVE2_CARDCOUNT; i++) {
            addCard(new Move2Card());
        }
        // Move3 Card
        for (int i = 0; i < MOVE3_CARDCOUNT; i++) {
            addCard(new Move3Card());
        }

        // Back Up Card
        for (int i = 0; i < BACKUP_CARDCOUNT; i++) {
            addCard(new BackUpCard());
        }

        // Left Turn Cards
        for (int i = 0; i < TURNLEFT_CARDCOUNT; i++) {
            addCard(new TurnLeftCard());
        }
        // Right Turn Cards
        for (int i = 0; i < TURNRIGHT_CARDCOUNT; i++) {
            addCard(new TurnRightCard());
        }
        // U-Turn Card
        for (int i = 0; i < UTURN_CARDCOUNT; i++) {
            addCard(new UTurnCard());
        }

        // Power Up Cards
        for (int i = 0; i < POWERUP_CARDCOUNT; i++) {
            addCard(new PowerUpCard());
        }

        // Again Card
        for (int i = 0; i < AGAIN_CARDCOUNT; i++) {
            addCard(new AgainCard());
        }
    }

    @Override
    public ArrayList<Card> getDeck() {
        return programmingDeck;
    }


}
