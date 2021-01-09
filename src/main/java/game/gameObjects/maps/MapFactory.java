package game.gameObjects.maps;

import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Tile;
import game.gameObjects.tiles.TileFactory;
import utilities.Coordinate;
import utilities.Utilities.AttributeType;

import java.util.ArrayList;

/**
 *
 */
public class MapFactory {
    private static MapFactory instance;

    private Tile [][] currentMap;
    private ArrayList<Map> maps;

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
        Tile[][] finalMap = new Tile[map.length][map.width];
        for (int i = 0; i < (map.length); i++) {
            for (int j = 0; j < (map.width); j++) {
                finalMap[i][j] = tileFactory.createTile(map.mapBlueprint[i][j]);
            }
        }
        setCurrentMap(finalMap);
        return finalMap;
    }

    /**
     * Adds the maps of the game to a ArrayList
     *
     * TODO find usage
     */
    private void generateMaps() {
        maps = new ArrayList<>();
        maps.add(new DizzyHighway());
    }

    public Tile[][] getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(Tile[][] currentMap) {
        this.currentMap = currentMap;
    }

}
