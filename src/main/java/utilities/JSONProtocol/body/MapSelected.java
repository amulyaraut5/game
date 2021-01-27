package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

import java.util.ArrayList;

public class MapSelected extends JSONBody {

    private String map;

    public MapSelected(String map) {
        this.map = map;
    }

    public String getMap() {
        return map;
    }
}
