package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class SelectionFinished extends JSONBody {
    private final int playerID;

    public SelectionFinished(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }
}
