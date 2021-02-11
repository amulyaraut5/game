package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class ConnectionUpdate extends JSONBody {
    private final int playerID;
    private final boolean connected;
    private final String action;

    public ConnectionUpdate(int id, boolean connected, String action) {
        playerID = id;
        this.connected = connected;
        this.action = action;
    }

    public int getID() {
        return playerID;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isConnected() {
        return connected;
    }

    public String getAction() {
        return action;
    }
}


