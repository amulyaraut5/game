package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class StartingPointTaken extends JSONBody {
    private int playerID;
    private int position;

    public StartingPointTaken(int playerID, int position) {
        this.playerID = playerID;
        this.position = position;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getPosition() {
        return position;
    }
}
