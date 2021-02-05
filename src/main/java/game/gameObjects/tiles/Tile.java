package game.gameObjects.tiles;

import utilities.enums.AttributeType;

import java.util.ArrayList;

/**
 * Represents the tile. Based on added attribute the different kinds of tiles are generated.
 * Attribute of a tile can be anything from wall, antenna, etc.
 *
 * @author Amulya
 */
public class Tile {

    /**
     * Saves one or multiple Attributes for one tile
     */
    private ArrayList<Attribute> attributes = new ArrayList<>();

    /**
     * Constructor for tiles.
     */
    public Tile() {
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<Attribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * Adds an Attribute to a tile by saving it in the ArrayList of attributes
     *
     * @param attribute added Attribute
     */
    public void addAttribute(Attribute attribute) {
        attributes.add(attribute);
    }

    public boolean hasAttribute(AttributeType type) {
        for (Attribute a : attributes) {
            if (a.getType() == type) return true;
        }
        return false;
    }
}