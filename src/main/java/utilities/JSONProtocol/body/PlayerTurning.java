package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;
import utilities.Orientation;

public class PlayerTurning extends JSONBody {

    int playerID;
    Orientation direction;

    public PlayerTurning(int playerID, Orientation direction) {
        this.playerID = playerID;
        this.direction = direction;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public Orientation getDirection() {
        return direction;
    }

    public void setDirection(Orientation direction) {
        this.direction = direction;
    }
}
