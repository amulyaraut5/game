package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class PlayerStatus extends JSONBody {
    private final int playerID;
    private final boolean ready;

    public PlayerStatus(int id, boolean ready) {
        playerID = id;
        this.ready = ready;
    }

    public int getID() {
        return playerID;
    }

    public boolean isReady() {
        return ready;
    }
}
