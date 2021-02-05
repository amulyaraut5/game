package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class GameWon extends JSONBody {
    private final int playerID;

    public GameWon(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }
}
