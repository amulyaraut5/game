package utilities.JSONProtocol.body;

import game.gameObjects.tiles.Tile;
import utilities.JSONProtocol.JSONBody;

import java.util.ArrayList;

public class GameStarted extends JSONBody {
    private ArrayList<ArrayList<Tile>> map;

    public GameStarted(ArrayList<ArrayList<Tile>> map) {
        this.map = map;
    }
}
