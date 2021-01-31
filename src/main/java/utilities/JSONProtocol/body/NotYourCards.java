package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class NotYourCards extends JSONBody {
    private final int playerID;
    private final int cards;

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
