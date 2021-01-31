package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class MapSelected extends JSONBody {

    private final String map;

    public MapSelected(String map) {
        this.map = map;
    }

    public String getMap() {
        return map;
    }
}
