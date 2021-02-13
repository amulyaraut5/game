package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class Movement extends JSONBody {
    private final int playerID;
    private final int to;

    public Movement(int playerID, int to) {
        this.playerID = playerID;
        this.to = to;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getTo() {
        return to;
    }
}
