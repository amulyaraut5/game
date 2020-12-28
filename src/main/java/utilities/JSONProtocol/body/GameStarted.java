package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;
import utilities.JSONProtocol.body.gameStarted.Maps;

import java.util.ArrayList;

public class GameStarted extends JSONBody {
    private ArrayList<Maps> map;

    public GameStarted(ArrayList<Maps> map) {
        this.map = map;
    }
}
