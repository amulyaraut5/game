package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class Energy extends JSONBody {
    private int playerID;
    private int count;

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
