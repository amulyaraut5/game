package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;
import utilities.enums.Rotation;

public class PlayerTurning extends JSONBody {
    private int playerID;
    private Rotation direction;

    public PlayerTurning(int playerID, Rotation rotation) {
        this.playerID = playerID;
        direction = rotation;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public Rotation getDirection() {
        return direction;
    }

    public void setDirection(Rotation direction) {
        this.direction = direction;
    }
}
