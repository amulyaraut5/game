package game.gameObjects.maps;

import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.RestartPoint;
import game.gameObjects.tiles.Tile;
import game.gameObjects.tiles.TileFactory;
import utilities.enums.AttributeType;

import java.util.ArrayList;

/**
 *
 */
public class MapBuilder {

    private static MapBuilder instance;
    private ArrayList<Blueprint> blueprints;

    private MapBuilder() {
        generateMaps();
    }

    public static MapBuilder getInstance() {
        if (instance == null) {
            instance = new MapBuilder();
        }
        return instance;
    }

    public static Map constructMap(Blueprint blueprint) {
        int max = 10;
        Tile[][] tiles = new Tile[max + 3][max];

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < max; y++) {
                StartZone startzone = new StartZone();
                tiles[x][y] = TileFactory.createTile(startzone.getMapBlueprint()[y][x]);
            }
        }
        for (int x = 0; x < max; x++) {
            for (int y = 0; y < max; y++) {
                tiles[x + 3][y] = TileFactory.createTile(blueprint.mapBlueprint[y][x]);
            }
        }
        if (!isRebootOnTiles(tiles)) {
            tiles[0][0].getAttributes().add(new RestartPoint());
        }
        return new Map(tiles);
    }

    public static boolean isRebootOnTiles(Tile[][] tiles) {
        for (int x = 0; x < (tiles.length); x++) {
            for (int y = 0; y < (tiles[0].length); y++) {
                for (Attribute a : tiles[x][y].getAttributes()) {
                    if (a.getType() == AttributeType.RestartPoint) {
                        return true;
                    }
                }
            }
        }
        return false;
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
