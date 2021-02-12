package utilities.JSONProtocol.body;

import utilities.BoardElement;
import utilities.JSONProtocol.JSONBody;

import java.util.ArrayList;

public class GameStarted extends JSONBody {
    private final ArrayList<BoardElement> map;

    public GameStarted(ArrayList<BoardElement> map) {
        this.map = map;
    }

    public ArrayList<BoardElement> getMap() {
        return map;
    }
}
