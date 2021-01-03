package utilities.JSONProtocol.body;

import game.gameObjects.tiles.Tile;
import utilities.JSONProtocol.JSONBody;
import utilities.JSONProtocol.body.gameStarted.BoardElement;

import java.util.ArrayList;

public class GameStarted extends JSONBody {
    private ArrayList<BoardElement> map;

    public GameStarted(ArrayList<BoardElement> map) {
        this.map = map;
    }

    public ArrayList<BoardElement> getMap(){
        return this.map;
    }
}
