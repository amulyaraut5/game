package game.gameObjects.maps;

import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Tile;
import utilities.Coordinate;
import utilities.Utilities;

import java.util.ArrayList;

/**
 * The map class helps in retrieving all the important aspects of the map.
 */
public class Map {

    private Tile[][] tiles;
    private ArrayList<Coordinate> laserCoordinates;
    private ArrayList<Coordinate> beltCoordinates;

    /**
     * Constructor that initializes the Tile[][] tiles.
     */
    public Map(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Retrieves the tile from the map.
     *
     * @param x x coordinate of the tile
     * @param y y coordinate of the tile
     * @return
     * @throws ArrayIndexOutOfBoundsException when the position is not on the map.
     */
    public Tile getTile(int x, int y) throws ArrayIndexOutOfBoundsException {
        return tiles[x][y];
    }

    /**
     * Retrieves the tile of the given position from the map
     *
     * @param pos position of tile
     * @return
     * @throws ArrayIndexOutOfBoundsException when the position is not on the map
     */
    public Tile getTile(Coordinate pos) throws ArrayIndexOutOfBoundsException {
        return tiles[pos.getX()][pos.getY()];
    }

    /**
     * This method stores all the laser coordinates from the actual
     * version of map and returns it.
     * The coordinates are retrieved only once for the actual map.
     * Usage <@Class Round.Laser> to find the path of lasers.
     *
     * @return
     */
    public ArrayList<Coordinate> getLaserCoordinates() {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if (a.getType() == Utilities.AttributeType.Laser) {
                        Coordinate temp = new Coordinate(i, j);
                        coordinates.add(temp);
                    }
                }
            }
        }
        return coordinates;
    }


    /**
     * Based on the type of attribute it returns the arraylist of coordinate of that specific
     * tile from the map.
     *
     * @return the coordinate
     */
    public ArrayList<Coordinate> lookInMapFor(Attribute attribute) {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if (a.getType() == attribute.getType()) {
                        coordinates.add(new Coordinate(i, j));
                        return coordinates;
                    }
                }
            }
        }
        return null;
    }

    public ArrayList<Coordinate> getBeltCoordinates(Tile[][] map) {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < (map.length); i++) {
            for (int j = 0; j < (map[0].length); j++) {
                for (Attribute a : map[i][j].getAttributes()) {
                    if (a.getType() == Utilities.AttributeType.Belt || a.getType() == Utilities.AttributeType.RotatingBelt) {
                        Coordinate temp = new Coordinate(i, j);
                        coordinates.add(temp);
                    }
                }
            }
        }
        return coordinates;
    }
}
