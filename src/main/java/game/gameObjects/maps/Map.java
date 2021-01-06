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

    private static ArrayList<Tile> finalMap;

    private ArrayList<Tile> laserTile;


    Map() {
        this.finalMap = new ArrayList<Tile>();

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
     * last Edited By: Amulya
     */
    public Tile getTile(int x, int y)  {
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
     * This methods adds the laser tiles from the map into arrayList.
     * Usage <@Class Round.Laser> to find the path of lasers.
     * @return
     * last Edited By: Amulya
     */
    public  ArrayList<Tile> getLaserTile(){
        return null;
        /*
        for(Tile tile : finalMap){
            if (tile.getAttribute().getType().equals("Laser")){
                laserTile.add(tile);
            }
            return laserTile;
        }
        return null;

         */
    }
}