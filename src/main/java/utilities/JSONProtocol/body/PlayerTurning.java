package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;
import utilities.Utilities;

public class PlayerTurning extends JSONBody {

    int playerID;
    Utilities.Orientation direction;

    public PlayerTurning(int playerID, Utilities.Orientation direction) {
        this.playerID = playerID;
        this.direction = direction;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public Utilities.Orientation getDirection() {
        return direction;
    }

    public void setDirection(Utilities.Orientation direction) {
        this.direction = direction;
    }
}
