package utilities.JSONProtocol.lobby;

import utilities.JSONProtocol.JSONBody;

public class PlayerAdded extends JSONBody {
    private int playerID;
    private String name;
    private int figure;

    public PlayerAdded(int id, String name, int figure) {
        this.playerID = id;
        this.name = name;
        this.figure = figure;
    }

    public int getId() {
        return playerID;
    }

    public String getName() {
        return name;
    }

    public int getFigure() {
        return figure;
    }
}
