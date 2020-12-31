package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class Energy extends JSONBody {

    int playerID;
    int count;

    public Energy(int playerID, int count) {
        this.playerID = playerID;
        this.count = count;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
