package game.gameObjects.maps;

import game.gameObjects.tiles.Tile;
import game.gameObjects.tiles.TileFactory;

import java.util.ArrayList;

/**
 *
 */
public class MapFactory {

    private static MapFactory instance;
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

    public static Map constructMap(Blueprint blueprint) {
        int max = 10;
        Tile[][] tiles = new Tile[max][max];

        for (int x = 0; x < max; x++) {
            for (int y = 0; y < max; y++) {
                tiles[x][y] = TileFactory.createTile(blueprint.mapBlueprint[x][y]);
            }
        }
        return new Map(tiles);
    }

    /**
     * returns every map in the game
     *
     * @return maps list containing every map in the game
     */
    public ArrayList<Blueprint> getBlueprints() {
        return blueprints;
    }

    /**
     * returns the map with the matching id
     *
     * @param id the id of the map to request
     * @return the requested map
     */
    public Blueprint getBlueprint(int id) {
        return blueprints.get(id);
    }

    /**
     * Adds the maps of the game to a ArrayList
     */
    private void generateMaps() {
        blueprints = new ArrayList<>();
        blueprints.add(new DizzyHighway());
        blueprints.add(new RiskyCrossing());
    }
}
