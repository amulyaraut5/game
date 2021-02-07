package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

import java.util.ArrayList;

public class MapSelected extends JSONBody {

    private final ArrayList<String> map;

    public MapSelected(String map) {

        ArrayList<String> mapAttribute = new ArrayList<>();
        mapAttribute.add(map);
        this.map = mapAttribute;
    }

    public ArrayList<String> getMap() {
        return map;
    }
}
