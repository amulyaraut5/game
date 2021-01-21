package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class CurrentPlayer extends JSONBody {
    private int playerID;

    public CurrentPlayer(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }

}
