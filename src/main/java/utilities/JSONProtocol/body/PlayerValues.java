package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class PlayerValues extends JSONBody {
    private final String name;
    private final int figure;

    public PlayerValues(String name, int figure) {
        this.name = name;
        this.figure = figure;
    }

    public String getName() {
        return name;
    }

    public int getFigure() {
        return figure;
    }
}
