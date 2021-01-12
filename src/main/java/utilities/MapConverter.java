package utilities;

import game.gameObjects.maps.Map;
import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Tile;
import utilities.JSONProtocol.body.GameStarted;
import utilities.JSONProtocol.body.gameStarted.BoardElement;

import java.util.ArrayList;

public abstract class MapConverter {

    public static GameStarted convert(Map map) {
        Tile[][] tiles = map.getTiles();
        ArrayList<BoardElement> mapList = new ArrayList<>();
        int xMax = tiles.length;
        int yMax = tiles[0].length;

        int position = 1;
        for (int x = 0; x < (xMax); x++) {
            for (int y = 0; y < (yMax); y++) {
                BoardElement temp = new BoardElement();
                temp.setField(tiles[x][y].getAttributes());
                temp.setPosition(position++);
                mapList.add(temp);
            }
        }
        return new GameStarted(mapList);
    }

    public static Map reconvert(GameStarted body) {
        int mapLength = 10;
        int index1;
        int index2;
        Tile[][] tiles = new Tile[mapLength][mapLength];
        ArrayList<BoardElement> JsonMap = body.getMap();
        for (BoardElement e : JsonMap) {
            index1 = calculateFirstIndex(e.getPosition(), mapLength);
            index2 = calculateSecondIndex(e.getPosition(), mapLength);
            Tile temp = new Tile();
            for (Attribute a : e.getField()) {
                temp.addAttribute(a);
            }
            tiles[index1][index2] = temp;
        }
        return new Map(tiles);
    }

    public static int calculateFirstIndex(int position, int mapLength) {
        int n = position / mapLength;
        if (position % mapLength == 0 && n != 0) {
            n = n - 1;
        }
        return n;
    }

    public static int calculateSecondIndex(int position, int mapLength) {
        int n = position % mapLength;
        if (n == 0) {
            n = mapLength;
        }
        n = n - 1;
        return n;
    }
}
