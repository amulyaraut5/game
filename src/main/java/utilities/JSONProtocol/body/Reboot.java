package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class Reboot extends JSONBody {
    private final int playerID;

    public Reboot(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }
}
