package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class Welcome extends JSONBody {

    private final int playerID;

    public Welcome(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }
}
