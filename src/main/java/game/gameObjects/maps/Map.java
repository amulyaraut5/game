package game.gameObjects.maps;

import game.gameObjects.tiles.*;
import utilities.Coordinate;
import utilities.enums.AttributeType;
import utilities.enums.Orientation;

import java.util.ArrayList;

/**
 * The map class helps in retrieving all the important aspects of the map either tiles from coordinate
 * or coordinates from tiles.
 *
 * @author Louis
 * @author Amulya
 */
public class Map {

    private final Tile[][] tiles;
    private final ArrayList<Coordinate> GreenBelts = new ArrayList<>();
    private final ArrayList<Coordinate> BlueBelts = new ArrayList<>();
    private Coordinate RestartPoint;
    private Coordinate antenna;

    /**
     * Constructor that initializes the Tile[][]
     */
    public Map(Tile[][] tiles) {
        this.tiles = tiles;
        readAll();
    }

    /**
     * Reads all coordinates that initially need to be read.(Store belts, restart point, and antenna for the specific map)
     */

    public void readAll() {
        readBeltCoordinates();
        readRestartPointCoordinate();
        readAntennaCoordinate();
    }

    /**
     * Checks whether the antenna or wall is blocking the movement or not.
     * @param coordinate coordinate of attribute where it needs to be checked
     * @param orientation orientation of wall
     * @return false if there is wall blocking, otherwise true
     */

    public boolean isWallBlocking(Coordinate coordinate, Orientation orientation) {
        boolean canMove = true;
        for (Attribute a : getTile(coordinate).getAttributes()) {
            if (a.getType() == AttributeType.Wall) {
                Wall temp = (Wall) a;
                if (!(temp.getOrientations() == null)) {
                    for (Orientation o : temp.getOrientations()) {
                        if (o == orientation) {
                            canMove = false;
                            break;
                        }
                    }
                }
            }
            if (a.getType() == AttributeType.Antenna) {
                canMove = false;
                break;
            }
        }
        return !canMove;
    }

    /**
     * This method retrieves the attribute from the coordinate in the map.
     * @param type
     * @param pos position to be checked
     * @return the attributeType
     */
    public Attribute getAttributeOn(AttributeType type, Coordinate pos) {
        if (!pos.isOutsideMap()) {
            Tile tile = tiles[pos.getX()][pos.getY()];
            for (Attribute attribute : tile.getAttributes()) {
                if (attribute.getType() == type) {
                    return attribute;
                }
            }
        }
        return null;
    }

    /**
     * This method checks whether there is antenna or pit on the given coordinate.
     * @param pos position to be checked
     * @return true if there is pit or antenna, otherwise false
     */

    public boolean isAttributeOn(int pos) {
        Coordinate coordinate = Coordinate.parse(pos);
        Tile tile = tiles[coordinate.getX()][coordinate.getY()];
        for (Attribute attribute : tile.getAttributes()) {
            if (attribute.getType() == AttributeType.Antenna || attribute.getType() == AttributeType.Pit) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method checks whether there is antenna or pit on the given coordinate.
     * @param pos coordinate to be checked
     * @return true if there is pit or antenna, otherwise false
     */

    public boolean isAttributeOn(Coordinate pos) {
        Tile tile = tiles[pos.getX()][pos.getY()];
        for (Attribute attribute : tile.getAttributes()) {
            if (attribute.getType() == AttributeType.Antenna || attribute.getType() == AttributeType.Pit) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the tile from the map.
     *
     * @param x x coordinate of the tile
     * @param y y coordinate of the tile
     * @return returns the tile
     * @throws ArrayIndexOutOfBoundsException when the position is not on the map.
     */
    public Tile getTile(int x, int y) throws ArrayIndexOutOfBoundsException {
        return tiles[x][y];
    }

    /**
     * Retrieves the tile of the given position from the map
     *
     * @param pos position of tile
     * @return returns the tile
     * @throws ArrayIndexOutOfBoundsException when the position is not on the map
     */
    public Tile getTile(Coordinate pos) throws ArrayIndexOutOfBoundsException {
        return tiles[pos.getX()][pos.getY()];
    }

    /**
     * This method initializes the antenna coordinate from the map.
     */

    public void readAntennaCoordinate() {
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if (a.getType() == AttributeType.Antenna) {
                        antenna = new Coordinate(i, j);
                        break;
                    }
                }
            }
        }
    }

    /**
     * This method initializes the restartPoint coordinates from the actual
     * version of map.
     */

    public void readRestartPointCoordinate() {
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if (a.getType() == AttributeType.RestartPoint) {
                        RestartPoint = new Coordinate(i, j);
                        break;
                    }
                }
            }
        }
    }

    /**
     * This method stores all the laser coordinates from the actual
     * version of map.
     */

    public ArrayList<Coordinate> readLaserCoordinates() {
        ArrayList<Coordinate> laser = new ArrayList<>();
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if (a.getType() == AttributeType.Laser) {
                        Coordinate temp = new Coordinate(i, j);
                        laser.add(temp);
                    }
                }
            }
        }
        return laser;
    }

    /**
     * This method stores all the push panel coordinates from the actual
     * version of map and returns it.
     */

    public ArrayList<Coordinate> readPushPanelCoordinate() {
        final ArrayList<Coordinate> pushPanel = new ArrayList<>();
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if (a.getType() == AttributeType.PushPanel) {
                        Coordinate temp = new Coordinate(i, j);
                        pushPanel.add(temp);
                    }
                }
            }
        }
        return pushPanel;
    }

    /**
     * This method stores all the control points coordinates from the actual
     * version of map and returns it.
     */

    public ArrayList<Coordinate> readControlPointCoordinate() {
        ArrayList<Coordinate> checkPoints = new ArrayList<>();
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if (a.getType() == AttributeType.ControlPoint) {
                        Coordinate temp = new Coordinate(i, j);
                        checkPoints.add(temp);
                    }
                }
            }
        }
        return sortCheckPoints(checkPoints);
    }

    /**
     * This method sorts all the checkPoints in the increasing order.
     * @param checkPoints arraylist of checkpoints to be sorted
     * @return sorted arrayList
     */

    private ArrayList<Coordinate> sortCheckPoints(ArrayList<Coordinate> checkPoints) {
        ArrayList<Coordinate> sortedPoints = new ArrayList<>();
        int i = 1;
        while (checkPoints.size() != sortedPoints.size()) {
            for (Coordinate point : checkPoints) {
                for (Attribute a : getTile(point).getAttributes()) {
                    if (a.getType() == AttributeType.ControlPoint) {
                        if (((ControlPoint) a).getCount() == i) {
                            sortedPoints.add(point);
                            i++;
                        }
                    }
                }
            }
        }
        return sortedPoints;
    }

    /**
     * This method stores all the gear coordinates from the actual
     * version of map and returns it.
     */

    public ArrayList<Coordinate> readGearCoordinate() {
        final ArrayList<Coordinate> gearCoordinates = new ArrayList<>();
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if (a.getType() == AttributeType.Gear) {
                        Coordinate temp = new Coordinate(i, j);
                        gearCoordinates.add(temp);
                    }
                }
            }
        }
        return gearCoordinates;
    }

    /**
     * Iterates through every tile of the map and stores all tiles that have a belt attribute in the particular list(One for green and one for blue belts)
     */
    public void readBeltCoordinates() {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if (a.getType() == AttributeType.Belt || a.getType() == AttributeType.RotatingBelt) {
                        Coordinate temp = new Coordinate(i, j);
                        coordinates.add(temp);
                    }
                }
            }
        }
        //Add Belts to Green and Blue Belt lists
        for (Coordinate c : coordinates) {
            for (Attribute a : getTile(c).getAttributes()) {
                if (a.getType() == AttributeType.Belt) {
                    Belt temp = (Belt) a;
                    if (temp.getSpeed() == 1) addGreenBelt(c);
                    else addBlueBelt(c);
                }
                if (a.getType() == AttributeType.RotatingBelt) {
                    RotatingBelt temp = (RotatingBelt) a;
                    if (temp.getSpeed() == 1) addGreenBelt(c);
                    else addBlueBelt(c);
                }
            }
        }
    }

    /**
     * This method stores all the energy space coordinates from the actual
     * version of map and returns it.
     * @return ArrayList of coordinates of energy space
     */

    public ArrayList<Coordinate> readEnergySpaceCoordinates() {
        final ArrayList<Coordinate> EnergySpaces = new ArrayList<>();
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if (a.getType() == AttributeType.EnergySpace) {
                        EnergySpaces.add(new Coordinate(i, j));
                    }
                }
            }
        }
        return EnergySpaces;
    }

    /**
     * This method stores all the startingPoint coordinates from the actual
     * version of map and returns it.
     * @return ArrayList of coordinates of startingPoint
     */

    public ArrayList<Coordinate> readStartingPointCoordinates() {
        final ArrayList<Coordinate> startingPoints = new ArrayList<>();
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if (a.getType() == AttributeType.StartPoint) {
                        startingPoints.add(new Coordinate(i, j));
                    }
                }
            }
        }
        return startingPoints;
    }

    private void addGreenBelt(Coordinate c) {
        GreenBelts.add(c);
    }

    private void addBlueBelt(Coordinate c) {
        BlueBelts.add(c);
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public ArrayList<Coordinate> getGreenBelts() {
        return GreenBelts;
    }

    public ArrayList<Coordinate> getBlueBelts() {
        return BlueBelts;
    }

    public Coordinate getRestartPoint() {
        return RestartPoint;
    }

    public Coordinate getAntenna() {
        return antenna;
    }

    public ArrayList<Coordinate> getStartingPoints() {
        return readStartingPointCoordinates();
    }
}
