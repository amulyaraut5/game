package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class NotYourCards extends JSONBody {
    private int playerID;
    private int cards;

    public NotYourCards(int playerID, int cards) {
        this.playerID = playerID;
        this.cards = cards;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getCards() {
        return cards;
    }
}
