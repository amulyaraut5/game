package Utilities.JSONProtocol.specialMessages;

import Utilities.JSONProtocol.JSONBody;

public class ConnectionUpdate extends JSONBody {

    private int id;
    private boolean connected;
    private String action;

    public ConnectionUpdate(int id, boolean connected, String action) {
        this.id = id;
        this.connected = connected;
        this.action = action;
    }

    public int getId() {
        return id;
    }

    public boolean isConnected() {
        return connected;
    }

    public String getAction() {
        return action;
    }
}


