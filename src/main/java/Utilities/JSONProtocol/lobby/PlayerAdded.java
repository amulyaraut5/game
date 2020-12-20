package Utilities.JSONProtocol.lobby;

import Utilities.JSONProtocol.JSONBody;

public class PlayerAdded extends JSONBody {
    private int id;
    private String name;
    private int figure;

    public PlayerAdded(int id, String name, int figure) {
        this.id = id;
        this.name = name;
        this.figure = figure;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getFigure() {
        return figure;
    }
}
