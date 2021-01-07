package game.gameObjects.maps;

import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Tile;
import game.gameObjects.tiles.TileFactory;
import utilities.Coordinate;

import java.util.ArrayList;

/**
 *
 */
public class MapFactory {
    private static MapFactory instance;
    private ArrayList<Map> maps;
    public final int ROW = 10;
    public final int COL = 10;

    Tile[][] finalMap = new Tile[ROW][COL];

    private MapFactory() {
        generateMaps();
    }

    public static MapFactory getInstance() {
        if (instance == null) {
            instance = new MapFactory();
        }
        return instance;
    }

    /**
     * returns every map in the game
     *
     * @return maps list containing every map in the game
     */
    public ArrayList<Map> getMaps() {
        return maps;
    }

    /**
     * returns the map with the matching id
     *
     * @param id the id of the map to request
     * @return the requested map
     */
    public Map getMap(int id) {
        return maps.get(id);
    }



    public Tile[][] constructMap(Map map) {
        TileFactory tileFactory = TileFactory.getInstance();
        for (int i = 0; i < (map.getLength()); i++) {
            for (int j = 0; j < (map.getWidth()); j++) {
                finalMap[i][j] = tileFactory.createTile(map.mapBlueprint[i][j]);
            }
        }
        return finalMap;
    }

    /**
     * Adds the maps of the game to a ArrayList
     */
    private void generateMaps() {
        maps = new ArrayList<>();
        maps.add(new DizzyHighway());
    }

    public ArrayList<Coordinate> getLaserCoordinates(){
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < (finalMap.length); i++) {
            for (int j = 0; j < (finalMap[0].length); j++) {
                for(Attribute a : finalMap[i][j].getAttributes()){
                    if(a.getType() == "Laser"){
                        Coordinate temp = new Coordinate(i,j);
                        coordinates.add(temp);
                    }
                }
            }
        }
        return coordinates;
    }


    /**
     * Retrieves the tile from the map.
     * @param x x coordinate of the tile
     * @param y y coordinate of the tile
     * @return
     * @throws ArrayIndexOutOfBoundsException when the position is not on the map.
     */
    public Tile getTile(int x, int y) throws ArrayIndexOutOfBoundsException  {
        return this.finalMap[x][y];
    }

    /**
     * Retrieves the tile of the given position from the map
     * @param pos position of tile
     * @return
     * @throws ArrayIndexOutOfBoundsException when the position is not on the map
     */
    public Tile getTile(Coordinate pos) throws ArrayIndexOutOfBoundsException {
        return this.finalMap[pos.getX()][pos.getY()];
    }

    /**
     * Retrieves the coordinate of the tile from the map
     * @return
     */
    public Coordinate lookInToMapFor(){
        return null;
    }
    /**
     * This methods adds the laser tiles from the map into arrayList.
     * Usage <@Class Round.Laser> to find the path of lasers.
     * @return ArrayList of tiles
     */

    public  ArrayList<Tile> getLaserTile(){

        ArrayList<Tile> laserTiles = new ArrayList<>();
        for (int i = 0; i < (finalMap.length); i++) {
            for (int j = 0; j < (finalMap[0].length); j++){
                for(Attribute a : finalMap[i][j].getAttributes()){
                    if(a.getType() == "Laser"){
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
