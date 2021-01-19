package utilities;

import game.gameObjects.cards.Card;
import utilities.enums.CardType;

/**
 * This class saves a player ID and a card.
 * It is used in Activation Phase to store the cards of all players for a particular register
 * and makes it possible to send as an ArrayList according to the protocol
 *
 * @author janau
 */

public class RegisterCard {

    private int playerID;
    private CardType cardName;

    public RegisterCard (int playerID, Card card) {
        this.playerID = playerID;
        this.cardName = card.getName();
    }

    public CardType getCard() {
        return cardName;
    }

    public int getPlayerID() {
        return playerID;
    }
}
