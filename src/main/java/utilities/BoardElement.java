package utilities;

import game.gameObjects.tiles.Attribute;

import java.util.ArrayList;

public class BoardElement {
    private int position;
    private ArrayList<Attribute> field = new ArrayList<>();

    public BoardElement(int position, ArrayList<Attribute> field) {
        this.position = position;
        this.field = field;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ArrayList<Attribute> getField() {
        return field;
    }

    public void setField(ArrayList<Attribute> field) {
        this.field = field;
    }
}