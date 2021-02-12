package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class DiscardHand extends JSONBody {
    private final int playerID;

    public DiscardHand(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }
}
