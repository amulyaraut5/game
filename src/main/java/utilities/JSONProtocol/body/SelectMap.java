package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

import java.util.ArrayList;

public class SelectMap extends JSONBody {
    private final ArrayList<String> availableMaps;

    public SelectMap(ArrayList<String> availableMaps) {
        this.availableMaps = availableMaps;
    }

    public ArrayList<String> getAvailableMaps() {
        return availableMaps;
    }
}
