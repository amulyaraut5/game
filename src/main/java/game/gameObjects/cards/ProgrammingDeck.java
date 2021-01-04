package game.gameObjects.cards;

import java.util.ArrayList;

public class ProgrammingDeck extends Deck {

    ArrayList<ProgrammingCard> deck;

    /** used in Programming phase, when players get 9 cards from their ProgrammingDeck*/

    public ArrayList<ProgrammingCard> drawProgrammingCards() {
        ArrayList<ProgrammingCard> tempDeck = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            tempDeck.add(deck.get(i));
            deck.remove(i);
        }
        return tempDeck;
    }
}
