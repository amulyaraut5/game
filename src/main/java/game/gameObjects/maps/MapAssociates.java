package game.gameObjects.maps;

import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Tile;
import utilities.Coordinate;
import utilities.Utilities;

import java.util.ArrayList;

public class MapAssociates {

    private Tile[][] map = MapFactory.getInstance().getCurrentMap();

    private static MapAssociates instance;

    public static MapAssociates getInstance() {
        if (instance == null) {
            instance = new MapAssociates();
        }
        return instance;
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
        return this.map[x][y];
    }

    /**
     * Retrieves the tile of the given position from the map
     *
     * @param pos position of tile
     * @return
     * @throws ArrayIndexOutOfBoundsException when the position is not on the map
     */
    public Tile getTile(Coordinate pos) throws ArrayIndexOutOfBoundsException {
        return this.map[pos.getX()][pos.getY()];
    }

    /**
     * This method stores all the laser coordinates from the actual
     * version of map and returns it.
     * The coordinates are retrieved only once for the actual map.
     * Usage Round.Laser class
     * @return
     */
    public ArrayList<Coordinate> getLaserCoordinates() {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < (map.length); i++) {
            for (int j = 0; j < (map[0].length); j++) {
                for (Attribute a : map[i][j].getAttributes()) {
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
        for (int i = 0; i < (map.length); i++) {
            for (int j = 0; j < (map[0].length); j++) {
                for (Attribute a : map[i][j].getAttributes()) {
                    if (a.getType() == attribute.getType()) {
                        coordinates.add(new Coordinate(i,j));
                        return coordinates;
                    }
                }
            }
        }
        return null;
    }

    /**
     * This methods adds the laser tiles from the map into arrayList.
     * Usage <@Class Round.Laser> to find the path of lasers.
     *
     * @return ArrayList of tiles
     */

    public ArrayList<Tile> getLaserTile() {

        ArrayList<Tile> laserTiles = new ArrayList<>();
        for (int i = 0; i < (map.length); i++) {
            for (int j = 0; j < (map[0].length); j++) {
                for (Attribute a : map[i][j].getAttributes()) {
                    if (a.getType() == Utilities.AttributeType.Laser) {
                        Tile tile = new Tile();
                        tile.addAttribute(a);
                        laserTiles.add(tile);
                    }
                }
            }
        }
        return laserTiles;
    }
}
