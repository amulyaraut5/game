package game.gameObjects.maps;

import game.gameObjects.tiles.Tile;
import game.gameObjects.tiles.TileFactory;

import java.util.ArrayList;

/**
 *
 */
public class MapFactory {
    private static MapFactory instance;

    private Tile [][] currentMap;
    private ArrayList<Blueprint> blueprints;

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
    public ArrayList<Blueprint> getMaps() {
        return blueprints;
    }

    /**
     * returns the map with the matching id
     *
     * @param id the id of the map to request
     * @return the requested map
     */
    public Blueprint getMap(int id) {
        return blueprints.get(id);
    }


    public Tile[][] constructMap(Blueprint blueprint) {

        TileFactory tileFactory = TileFactory.getInstance();
        Tile[][] finalMap = new Tile[blueprint.length][blueprint.width];
        for (int i = 0; i < (blueprint.length); i++) {
            for (int j = 0; j < (blueprint.width); j++) {
                finalMap[i][j] = tileFactory.createTile(blueprint.mapBlueprint[i][j]);
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
        blueprints = new ArrayList<>();
        blueprints.add(new DizzyHighway());
    }

    public Tile[][] getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(Tile[][] currentMap) {
        this.currentMap = currentMap;
    }

}
