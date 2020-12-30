package utilities.JSONProtocol.body.gameStarted;

import java.util.ArrayList;

import game.gameObjects.tiles.Attribute;
import utilities.JSONProtocol.body.gameStarted.Field;

public class Maps {
    private int position;
    private ArrayList<Attribute> field;

    public Maps(int position, ArrayList<Attribute> field){
        this.position = position;
        this.field = field;
    }
}
