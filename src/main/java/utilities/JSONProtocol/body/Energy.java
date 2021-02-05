package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class Energy extends JSONBody {
    private final int playerID;
    private final int count;

    public Energy(int playerID, int count) {
        this.playerID = playerID;
        this.count = count;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getCount() {
        return count;
    }
}
