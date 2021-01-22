package game.gameObjects.maps;

import game.gameObjects.tiles.*;
import utilities.Coordinate;
import utilities.enums.AttributeType;

import java.util.ArrayList;

/**
 * The map class helps in retrieving all the important aspects of the map either tiles from coordinate
 * or coordinates from tiles.
 */
public class Map {

    private Tile[][] tiles;
    private ArrayList<Coordinate> GreenBelts = new ArrayList<>();
    private ArrayList<Coordinate> BlueBelts = new ArrayList<>();
    private ArrayList<Coordinate> EnergySpaces = new ArrayList<>();
    private ArrayList<Coordinate> pushPanel = new ArrayList<>();
    private ArrayList<Coordinate> controlPointCoordinates = new ArrayList<>();
    private ArrayList<Coordinate> pitCoordinates = new ArrayList<>();
    private ArrayList<Coordinate> gearCoordinates = new ArrayList<>();
    private ArrayList<Coordinate> laserCoordinates = new ArrayList<>();

    private Coordinate RestartPoint;

    private Coordinate antenna;


    /**
     * Constructor that initializes the Tile[][]
     */
    public Map(Tile[][] tiles) {
        this.tiles = tiles;
    }

    /**
     * Getter for tiles
     * @return returns the 2 dimensional array of tiles
     */
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
        return  laser;
    }



    /**
     * This method initializes the restartPoint coordinates from the actual
     * version of map and returns it.
     */
    // TODO Case for multiple restart coordinates is not handled

    public void readRestartPointCoordinate() {
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if (a.getType() == AttributeType.RestartPoint) {
                        this.RestartPoint = new Coordinate(i, j);
                        break;
                    }
                }
            }
        }
    }


    /**
     * This method stores all the laser coordinates from the actual
     * version of map and returns it.
     */
    public void readPushPanelCoordinate() {

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
    }

    /**
     * This method stores all the control points coordinates from the actual
     * version of map and returns it.
     */
    public void readControlPointCoordinate() {
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if (a.getType() == AttributeType.ControlPoint) {
                        Coordinate temp = new Coordinate(i, j);
                        controlPointCoordinates.add(temp);
                    }
                }
            }
        }
    }

    /**
     * This method stores all the pit coordinates from the actual
     * version of map and returns it.
     */
    public void readPitCoordinate() {
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if (a.getType() == AttributeType.ControlPoint) {
                        Coordinate temp = new Coordinate(i, j);
                        pitCoordinates.add(temp);
                    }
                }
            }
        }
    }

    /**
     * This method stores all the pit coordinates from the actual
     * version of map and returns it.
     */
    public void readGearCoordinate() {
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if (a.getType() == AttributeType.ControlPoint) {
                        Coordinate temp = new Coordinate(i, j);
                        gearCoordinates.add(temp);
                    }
                }
            }
        }
    }





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
                    if (temp.getSpeed() == 1) {
                        addGreenBelt(c);
                    } else {
                        addBlueBelt(c);
                    }
                }
                if (a.getType() == AttributeType.RotatingBelt) {
                    RotatingBelt temp = (RotatingBelt) a;
                    if (temp.getSpeed() == 1) {
                        addGreenBelt(c);
                    } else {
                        addBlueBelt(c);
                    }
                }
            }

        }
    }

    // TODO Delete Later NO Uasage
    public void readEnergySpaceCoordinates() {
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if(a.getType() == AttributeType.EnergySpace){
                        this.EnergySpaces.add(new Coordinate(i, j));
                    }
                }
            }
        }
    }

    public void readAll() {
        readBeltCoordinates();
        readRestartPointCoordinate();
        readEnergySpaceCoordinates();
        readPitCoordinate();
        readGearCoordinate();
        readPushPanelCoordinate();
        readLaserCoordinates();
        readControlPointCoordinate();
    }


    private void addGreenBelt(Coordinate c) {
        GreenBelts.add(c);
    }

    private void addBlueBelt(Coordinate c) {
        BlueBelts.add(c);
    }

    public ArrayList<Coordinate> getGreenBelts() {
        return GreenBelts;
    }

    public ArrayList<Coordinate> getBlueBelts() {
        return BlueBelts;
    }

    public Coordinate getRestartPoint() {
        return this.RestartPoint;
    }

    public ArrayList<Coordinate> getEnergySpaces() {
        return EnergySpaces;
    }

    public ArrayList<Coordinate> getPushPanel() { return pushPanel; }

    public ArrayList<Coordinate> getControlPointCoordinates() { return controlPointCoordinates; }

    public ArrayList<Coordinate> getPitCoordinates() { return pitCoordinates; }

    public ArrayList<Coordinate> getGearCoordinates() { return gearCoordinates; }

    public ArrayList<Coordinate> getLaserCoordinates(){ return laserCoordinates; }

    public Coordinate getAntenna() {
        return antenna;
    }
}
