package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class ShuffleCoding extends JSONBody {
    private final int playerID;

    public ShuffleCoding(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }
}
