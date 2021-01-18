package utilities;

import game.gameObjects.cards.Card;

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
