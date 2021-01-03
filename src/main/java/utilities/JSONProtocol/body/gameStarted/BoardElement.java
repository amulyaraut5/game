package utilities.JSONProtocol.body.gameStarted;

import game.gameObjects.tiles.Attribute;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BoardElement {
    private int position;
    private ArrayList<Attribute> field = new ArrayList<>();


    public int getPosition(){
        return this.position;
    }

    public ArrayList<Attribute> getField(){
        return this.field;
    }

    public void addAttribute(Attribute a){
        field.add(a);
    }

    public void setPosition(int position){
        this.position = position;
    }
}
