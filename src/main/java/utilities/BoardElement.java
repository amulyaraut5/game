package utilities;

import game.gameObjects.tiles.Attribute;

import java.util.ArrayList;
/**
 * One instance of this class represents one tile in the GameStarted map attribute.
 *
 * @author Louis
 */

public class BoardElement {
    /**
     * Position of the tile.
     */
    private int position;
    /**
     * Attributes of the tile
     */
    private ArrayList<Attribute> field;

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
