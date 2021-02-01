package utilities;

import game.gameObjects.maps.Map;
import game.gameObjects.maps.MapBuilder;
import game.gameObjects.maps.StartZone;
import game.gameObjects.tiles.RestartPoint;
import game.gameObjects.tiles.Tile;
import game.gameObjects.tiles.TileFactory;
import utilities.JSONProtocol.body.GameStarted;
import utilities.JSONProtocol.body.gameStarted.BoardElement;

import java.util.ArrayList;

public abstract class MapConverter {

    public static GameStarted convert(Map map) {
        Tile[][] tiles = map.getTiles();
        ArrayList<BoardElement> mapList = new ArrayList<>();
        int xMax = Utilities.MAP_WIDTH;
        int yMax = Utilities.MAP_HEIGHT;

        for (int y = 0; y < yMax; y++) {
            for (int x = 3; x < xMax; x++) {
                BoardElement temp = new BoardElement();
                temp.setField(tiles[x][y].getAttributes());
                temp.setPosition(y * xMax + x);
                mapList.add(temp);
            }
        }
        return new GameStarted(mapList);
    }

    public static Map reconvert(GameStarted body) {
        ArrayList<BoardElement> JsonMap = body.getMap();
        int xMax = Utilities.MAP_WIDTH;
        int yMax = Utilities.MAP_HEIGHT;
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
            tiles[0][0].getAttributes().add(new RestartPoint());
        }
        return new Map(tiles);
    }
}
