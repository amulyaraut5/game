package game.gameObjects.tiles;

import java.util.ArrayList;

public class Tile {

    /**
     * saves one or multiple Attributes for one tile
     */
    private ArrayList<Attribute> attributes = new ArrayList<>();;

    /**
     * Constructor for tiles.
     */

    public Tile() {
    }


    public ArrayList<Attribute> getAttributes() {
        return this.attributes;
    }

    /**
     * Adds an Attribute to a tile by saving it in the ArrayList of attributes
     *
     * @param attribute added Attribute
     */
    public void addAttribute(Attribute attribute) {
        attributes.add(attribute);
    }

    /**
     * It sets the priority order of execution of different attributes of tiles.
     */
    public void priorityOrder() {
        //TODO
    }


    /**
     * This method creates the tile with specific attribute.
     * Every tile has then it's own specific id which can be called while laying out the map structure.
     * Depending upon the needs of map, we can create our own tile with multiple attributes with different orientations.
     *
     * @param
     * @return tile
     */


    public boolean isTileOccupied() {
        return true;
    }
}