package utilities;

import game.gameObjects.cards.Card;

/**
 * This class saves a player ID and a card.
 * It is used in Activation Phase to store the cards of all players for a particular register
 * and makes it possible to send as an ArrayList according to the protocol
 *
 * @author janau
 */

public class RegisterCard {

    private int playerID;
    private Card card;

    public RegisterCard (int playerID, Card card) {
        this.playerID = playerID;
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }
}
