package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class ShuffleCoding extends JSONBody {
    private int playerID;

    public ShuffleCoding(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }
}
