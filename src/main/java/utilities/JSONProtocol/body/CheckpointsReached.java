package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class CheckpointsReached extends JSONBody {

    int playerID;

    public CheckpointsReached(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }
}
