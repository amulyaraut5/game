package Utilities.JSONProtocol.lobby;

import Utilities.JSONProtocol.JSONBody;

public class PlayerStatus extends JSONBody {
    private int id;
    private boolean ready;

    public PlayerStatus(int id, boolean ready) {
        this.id = id;
        this.ready = ready;
    }

    public int getId() {
        return id;
    }

    public boolean isReady() {
        return ready;
    }
}
