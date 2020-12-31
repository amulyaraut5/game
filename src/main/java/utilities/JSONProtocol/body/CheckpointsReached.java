package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class CheckpointsReached extends JSONBody {

    int playerID;
    int number;

    public CheckpointsReached(int playerID, int number) {

        this.playerID = playerID;
        this.number = number;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
