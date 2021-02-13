package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class PlayerAdded extends JSONBody {
    private final int playerID;
    private final String name;
    private final int figure;

    public PlayerAdded(int id, String name, int figure) {
        playerID = id;
        this.name = name;
        this.figure = figure;
    }

    public int getID() {
        return playerID;
    }

    public String getName() {
        return name;
    }

    public int getFigure() {
        return figure;
    }
}
