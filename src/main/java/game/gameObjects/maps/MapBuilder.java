package game.gameObjects.maps;

import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.RestartPoint;
import game.gameObjects.tiles.Tile;
import game.gameObjects.tiles.TileFactory;
import utilities.enums.AttributeType;
import utilities.enums.Orientation;

/**
 * This class is used to initialize the map.
 *
 * @author simon
 */
public final class MapBuilder {
    private MapBuilder() {
    }

    /**
     * Constructs the map from a blueprint.
     * @param blueprint blueprint that map gets constructed with.
     * @return returns the resulting map instance.
     */
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
            tiles[0][0].getAttributes().add(new RestartPoint(Orientation.RIGHT));
        }
        return new Map(tiles);
    }

    /**
     * Checks if there is a reboot point in a particular 2d array of tiles.
     * @param tiles tiles that are checked for a reboot point
     * @return returns a boolean value, indicating whether there is a reboot point on the input tiles or not.
     */

    @SuppressWarnings("ForLoopReplaceableByForEach")
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
}
