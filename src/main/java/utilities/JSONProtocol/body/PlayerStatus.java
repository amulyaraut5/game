package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class PlayerStatus extends JSONBody {
    private int playerID;
    private boolean ready;

    public PlayerStatus(int id, boolean ready) {
        this.playerID = id;
        this.ready = ready;
    }

    public int getID() {
        return playerID;
    }

    public boolean isReady() {
        return ready;
    }
}
