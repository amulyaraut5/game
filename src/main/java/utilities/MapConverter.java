package utilities;

import game.gameObjects.maps.Map;
import game.gameObjects.maps.MapBuilder;
import game.gameObjects.maps.StartZone;
import game.gameObjects.tiles.RestartPoint;
import game.gameObjects.tiles.Tile;
import game.gameObjects.tiles.TileFactory;
import utilities.JSONProtocol.body.GameStarted;
import utilities.enums.Orientation;

import java.util.ArrayList;

/**
 * This class is used to convert the map, which is a 2 dimensional array on the server side, into the protocol GameStarted message and back.
 *
 * @author Louis
 */
public final class MapConverter {

    /**
     * Converts an instance of the map class into a GameStarted protocol message body.
     * @param map Map instance that is converted
     * @return resulting GameStarted message body
     */
    public static GameStarted convert(Map map) {
        Tile[][] tiles = map.getTiles();
        ArrayList<BoardElement> mapList = new ArrayList<>();
        int xMax = Constants.MAP_WIDTH;
        int yMax = Constants.MAP_HEIGHT;

        for (int y = 0; y < yMax; y++) {
            for (int x = 3; x < xMax; x++) {
                BoardElement temp = new BoardElement(y * xMax + x, tiles[x][y].getAttributes());
                mapList.add(temp);
            }
        }
        return new GameStarted(mapList);
    }

    /**
     * Converts a GameStarted message body back to an instance of the map class.
     * @param body GameStarted message body that is converted to a map object.
     * @return resulting map instance
     */
    public static Map reconvert(GameStarted body) {
        ArrayList<BoardElement> JsonMap = body.getMap();
        int xMax = Constants.MAP_WIDTH;
        int yMax = Constants.MAP_HEIGHT;
        Tile[][] tiles = new Tile[xMax][yMax];

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < yMax; y++) {
                StartZone startzone = new StartZone();
                tiles[x][y] = TileFactory.createTile(startzone.getMapBlueprint()[y][x]);
            }
        }
        for (BoardElement e : JsonMap) {
            int pos = e.getPosition();
            int x = pos % xMax;
            int y = pos / xMax;
            Tile temp = new Tile();
            temp.setAttributes(e.getField());
            tiles[x][y] = temp;
        }
        if (!MapBuilder.isRebootOnTiles(tiles)) {
            tiles[0][0].getAttributes().add(new RestartPoint(Orientation.RIGHT));
        }
        return new Map(tiles);
    }
}
