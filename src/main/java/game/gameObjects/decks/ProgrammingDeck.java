package game.gameObjects.decks;

import game.gameObjects.cards.*;

import java.util.ArrayList;

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
        for (int i = 0; i < 5; i++) {
            addCard(new Move1Card());
        }
        // Move 2 Cards
        for (int i = 0; i < 3; i++) {
            addCard(new Move2Card());
        }
        // Move3 Card
        addCard(new Move3Card());

        // Move Back Card
        addCard(new BackUpCard());

        // Left Turn Cards
        for (int i = 0; i < 3; i++) {
            addCard(new TurnLeftCard());
        }
        // Right Turn Cards
        for (int i = 0; i < 3; i++) {
            addCard(new TurnRightCard());
        }
        // U-Turn Card
        addCard(new UTurnCard());

        // Power Up Cards
        addCard(new PowerUpCard());

        // Again Card
        for (int i = 0; i < 2; i++) {
            addCard(new AgainCard());
        }
    }

    @Override
    public ArrayList<Card> getDeck() {
        return programmingDeck;
    }


}
