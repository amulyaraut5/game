package utilities.JSONProtocol.connection;

import utilities.JSONProtocol.JSONBody;

public class Welcome extends JSONBody {

    private static int idCounter = 0;
    private int playerID;

    public Welcome() {
        playerID = idCounter++;
    }

    public Welcome(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerId() {
        return this.playerID;
    }

    public void setId(int id) {
        this.playerID = playerID;
    }
}
