package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class GameWon extends JSONBody {

    int playerID;

    public GameWon(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }
}
