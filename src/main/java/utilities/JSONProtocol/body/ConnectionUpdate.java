package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class ConnectionUpdate extends JSONBody {
    private int playerID;
    private boolean connected;
    private String action;

    public ConnectionUpdate(int id, boolean connected, String action) {
        this.playerID = id;
        this.connected = connected;
        this.action = action;
    }

    public int getID() {
        return playerID;
    }

    public boolean isConnected() {
        return connected;
    }

    public String getAction() {
        return action;
    }
}


