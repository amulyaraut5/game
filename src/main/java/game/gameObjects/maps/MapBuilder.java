package game.gameObjects.maps;

import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.RestartPoint;
import game.gameObjects.tiles.Tile;
import game.gameObjects.tiles.TileFactory;
import utilities.enums.AttributeType;
import utilities.enums.Orientation;

/**
 * TODO
 */
public final class MapBuilder {
    private MapBuilder() {
    }

    /**
     * TODO
     * @param blueprint
     * @return
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
     * TODO
     * @param tiles
     * @return
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
