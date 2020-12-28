package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class PlayerValues extends JSONBody {
    private String name;
    private int figure;

    public PlayerValues(String name, int figure){
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
