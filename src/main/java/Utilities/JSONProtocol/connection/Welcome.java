package Utilities.JSONProtocol.connection;

import Utilities.JSONProtocol.JSONBody;

public class Welcome extends JSONBody {

    private static int idCounter = 0;
    private int playerID;

    public Welcome() {
        playerID = idCounter++;
    }

    public Welcome(int id) {
        this.playerID = id;
    }

    public int getId() {
        return playerID;
    }

    public void setId(int id) {
        this.playerID = id;
    }
}
