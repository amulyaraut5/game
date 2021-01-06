package game.gameObjects.maps;

import game.gameObjects.tiles.Tile;
import utilities.Coordinate;
import utilities.Utilities;

import java.util.ArrayList;

public abstract class Map {

    protected String name;
    protected String gameLength;
    protected int minPlayers;
    protected int maxPlayers;
    protected Utilities.Difficulty difficulty;
    protected Object[][] mapBlueprint;
    protected int width;
    protected int length;

    public Object[][] getMapBlueprint() {
        return mapBlueprint;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Returns the tile with specific id.
     * @param
     * @return
     * last Edited By: Amulya
     */
    /*
    public Tile getTile(int id){
        return finalMap.get(id);
    }



    /**
     * Returns tile position from the map
     * @return
     * last Edited By: Amulya
     */

    public Coordinate getTilePosition(Tile tile){
        return null;
    }



}

