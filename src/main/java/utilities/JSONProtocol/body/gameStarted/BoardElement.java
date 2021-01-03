package utilities.JSONProtocol.body.gameStarted;

import game.gameObjects.tiles.Attribute;

import java.util.ArrayList;

public class BoardElement {
    private int position;
    private ArrayList<Attribute> field = new ArrayList<>();

    public BoardElement() {
    }

    public BoardElement(int position, ArrayList<Attribute> field) {
        this.position = position;
        this.field = field;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ArrayList<Attribute> getField() {
        return this.field;
    }

    public void addAttribute(Attribute a) {
        field.add(a);
    }
}
