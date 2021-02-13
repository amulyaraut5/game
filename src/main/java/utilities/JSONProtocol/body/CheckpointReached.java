package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class CheckpointReached extends JSONBody {
    private int playerID;
    private int number;

    public CheckpointReached(int playerID, int number) {

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
