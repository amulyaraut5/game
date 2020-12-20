package Utilities.JSONProtocol.lobby;

import Utilities.JSONProtocol.JSONBody;

public class SetStatus extends JSONBody {
    private boolean ready;

    public SetStatus(boolean ready){
        this.ready = ready;
    }

    public boolean isReady() {
        return ready;
    }
}
