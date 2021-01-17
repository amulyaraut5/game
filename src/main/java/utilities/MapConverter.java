package utilities;

import game.Player;
import game.gameObjects.maps.Map;
import game.gameObjects.maps.StartZone;
import game.gameObjects.tiles.Tile;
import game.gameObjects.tiles.TileFactory;
import utilities.JSONProtocol.body.GameStarted;
import utilities.JSONProtocol.body.Movement;
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
        return new Map(tiles);
    }

    public static Movement convertCoordinate(Player player, Coordinate coordinate){
        int position = coordinate.getY() * Utilities.MAP_WIDTH + coordinate.getX() + 1;
        return new Movement(player.getID(), position);
    }

    public static Coordinate reconvertToCoordinate(int position){
        int xMax = Utilities.MAP_WIDTH;
        int x = position % xMax;
        int y = position / xMax;
        Coordinate coordinate = new Coordinate(x, y);
        return coordinate;
    }
}
