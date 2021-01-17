package game.gameObjects.maps;

import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Belt;
import game.gameObjects.tiles.RotatingBelt;
import game.gameObjects.tiles.Tile;
import utilities.Coordinate;
import utilities.Utilities;

import java.util.ArrayList;

/**
 * The map class helps in retrieving all the important aspects of the map.
 */
public class Map {

    private Tile[][] tiles;
    private ArrayList<Coordinate> GreenBelts = new ArrayList<>();
    private ArrayList<Coordinate> BlueBelts = new ArrayList<>();
    private ArrayList<Coordinate> EnergySpaces = new ArrayList<>();
    private Coordinate RestartPoint;


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
     * This method returns the coordinate for the RestartPoint from the map.
     * @return
     */
    public Coordinate getRestartPointCoordinate(){
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if (a.getType() == Utilities.AttributeType.Laser) {
                       return new Coordinate(i, j);
                    }
                }
            }
        }
        return null;
    }

    /**
     *
     * @return
     */
    public ArrayList<Coordinate> getEnergySpaceCoordinate(){
        ArrayList<Coordinate> energyCoordinates = new ArrayList<>();
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if (a.getType() == Utilities.AttributeType.EnergySpace) {
                        Coordinate temp = new Coordinate(i, j);
                        energyCoordinates.add(temp);
                    }
                }
            }
        }
        return energyCoordinates;
    }

    /**
     *
     * @return
     */
    public ArrayList<Coordinate> getPushPanelCoordinate(){
        ArrayList<Coordinate> pushPanelCoordinates = new ArrayList<>();
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if (a.getType() == Utilities.AttributeType.PushPanel) {
                        Coordinate temp = new Coordinate(i, j);
                        pushPanelCoordinates.add(temp);
                    }
                }
            }
        }
        return pushPanelCoordinates;
    }

    public ArrayList<Coordinate> getControlPointCoordinate(){
        ArrayList<Coordinate> controlPointCoordinates = new ArrayList<>();
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if (a.getType() == Utilities.AttributeType.ControlPoint) {
                        Coordinate temp = new Coordinate(i, j);
                        controlPointCoordinates.add(temp);
                    }
                }
            }
        }
        return controlPointCoordinates;
    }

    public ArrayList<Coordinate> getPitCoordinate(){
        ArrayList<Coordinate> pitCoordinates = new ArrayList<>();
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if (a.getType() == Utilities.AttributeType.ControlPoint) {
                        Coordinate temp = new Coordinate(i, j);
                        pitCoordinates.add(temp);
                    }
                }
            }
        }
        return pitCoordinates;
    }

    public ArrayList<Coordinate> getGearCoordinate(){
        ArrayList<Coordinate> gearCoordinates = new ArrayList<>();
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if (a.getType() == Utilities.AttributeType.ControlPoint) {
                        Coordinate temp = new Coordinate(i, j);
                        gearCoordinates.add(temp);
                    }
                }
            }
        }
        return gearCoordinates;
    }


    public void readAll(){
        readBeltCoordinates();
        readRestartPointCoordinate();
        readEnergySpaceCoordinates();
    }


    public void readBeltCoordinates() {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if (a.getType() == Utilities.AttributeType.Belt || a.getType() == Utilities.AttributeType.RotatingBelt) {
                        Coordinate temp = new Coordinate(i, j);
                        coordinates.add(temp);
                    }
                }
            }
        }

        //Add Belts to Green and Blue Belt lists
        for (Coordinate c : coordinates) {
            for (Attribute a : getTile(c).getAttributes()) {
                if(a.getType() == Utilities.AttributeType.Belt){
                    Belt temp = (Belt) a;
                    if(temp.getSpeed() == 1){
                        addGreenBelt(c);
                    }
                    else{
                        addBlueBelt(c);
                    }
                }
                if(a.getType() == Utilities.AttributeType.RotatingBelt){
                    RotatingBelt temp = (RotatingBelt) a;
                    if(temp.getSpeed() == 1){
                        addGreenBelt(c);
                    }
                    else{
                        addBlueBelt(c);
                    }
                }
            }

        }
    }
    public void readRestartPointCoordinate() {
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if(a.getType() == Utilities.AttributeType.RestartPoint){
                        this.RestartPoint = new Coordinate(i, j);
                        break;
                    }
                }
            }
        }
    }

    public void readEnergySpaceCoordinates(){
        for (int i = 0; i < (tiles.length); i++) {
            for (int j = 0; j < (tiles[0].length); j++) {
                for (Attribute a : tiles[i][j].getAttributes()) {
                    if(a.getType() == Utilities.AttributeType.EnergySpace){
                        this.EnergySpaces.add(new Coordinate(i, j));
                    }
                }
            }
        }
    }


    private void addGreenBelt(Coordinate c){
        GreenBelts.add(c);
    }

    private void addBlueBelt(Coordinate c){
        BlueBelts.add(c);
    }

    public ArrayList<Coordinate> getGreenBelts(){
        return GreenBelts;
    }

    public ArrayList<Coordinate> getBlueBelts(){
        return BlueBelts;
    }

    public Coordinate getRestartPoint(){
        return this.RestartPoint;
    }

    public ArrayList<Coordinate> getEnergySpaces(){
        return EnergySpaces;
    }
}
