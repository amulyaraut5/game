package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class CardPlayed extends JSONBody {
    private final int playerID;
    private final String card;

    public CardPlayed(int playerID, String card) {
        this.playerID = playerID;
        this.card = card;
    }

    public int getPlayerID() {
        return playerID;
    }

    public String getCard() {
        return card;
    }
}
