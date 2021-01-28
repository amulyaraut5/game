package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

import java.util.ArrayList;

public class SelectMap extends JSONBody {
    private ArrayList<String> map;

    public SelectMap(ArrayList<String> map) {
        this.map = map;
    }

    public ArrayList<String> getMap() {
        return map;
    }
}
