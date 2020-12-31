package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class CardSelected extends JSONBody {
    int playerID;
    int register;

    public CardSelected(int playerID, int register) {
        this.playerID = playerID;
        this.register = register;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getRegister() {
        return register;
    }
}
