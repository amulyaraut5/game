package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class Welcome extends JSONBody {

    private int playerID;

    public Welcome(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }
}
