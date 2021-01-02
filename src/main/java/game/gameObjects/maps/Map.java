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
    protected ArrayList<Coordinate> startPositions;
    protected int[][] mapBlueprint;
    protected int width;
    protected int length;

    private static ArrayList<Tile> finalMap;

    private ArrayList<Tile> laserTile;


    Map() {
        this.finalMap = new ArrayList<Tile>();

    }


    public static void generateMap(Map map) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                finalMap.add(Tile.getInstance().createTile(map.mapBlueprint[i][j]));
                System.out.println(j);
                System.out.println(i);
            }
        }
    }

    public static ArrayList<Tile> getMap() {

        return finalMap;
    }

    /**
     * Returns the tile with specific id.
     * @param id
     * @return
     * last Edited By: Amulya
     */
    public Tile getTile(int id){
        return finalMap.get(id);
    }

    /**
     * Returns the tile at specified position.
     * @param x
     * @param y
     * @return
     * @throws ArrayIndexOutOfBoundsException
     * last Edited By: Amulya
     */
    public Tile getTile(int x, int y) throws ArrayIndexOutOfBoundsException {
        return null;
    }

    /**
     * Returns tile position from the map
     * @return
     * last Edited By: Amulya
     */
    public Coordinate getTilePosition(Tile tile){
        return null;
    }

    /**
     * This methods adds the laser from the map into arrayList of array.
     * Usage <@Class Round.Laser> to find the path of lasers.
     * @return
     * @author Amulya
     * last Edited By: Amulya
     */
    public  ArrayList<Tile> getLaserTile(){
        for(int i = 61; i<= 68; i++){
            laserTile.add(this.getTile(i));
            return laserTile;
        }
        return null;
    }
}