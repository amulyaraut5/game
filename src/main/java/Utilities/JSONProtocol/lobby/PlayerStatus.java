package Utilities.JSONProtocol.lobby;

import Utilities.JSONProtocol.JSONBody;

public class PlayerStatus extends JSONBody {
    private int playerID;
    private boolean ready;

    public PlayerStatus(int id, boolean ready) {
        this.playerID = id;
        this.ready = ready;
    }

    public int getId() {
        return playerID;
    }

    public boolean isReady() {
        return ready;
    }
}
