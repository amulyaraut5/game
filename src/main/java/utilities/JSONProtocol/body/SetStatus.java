package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class SetStatus extends JSONBody {
    private boolean ready;

    public SetStatus(boolean ready){
        this.ready = ready;
    }

    public boolean isReady() {
        return ready;
    }
}
