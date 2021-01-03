package utilities.JSONProtocol;

import game.gameObjects.maps.MapFactory;
import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Tile;
import game.gameObjects.tiles.TileFactory;
import utilities.JSONProtocol.body.GameStarted;
import utilities.JSONProtocol.body.gameStarted.BoardElement;

import java.util.ArrayList;

public class MapConverter {
    //TODO change Length and Width attributes if Map is not DizzyHighway
    public int mapLength = 10;
    public int mapWidth = 10;
    private static MapConverter instance;

    public static MapConverter getInstance(){
        if (instance == null) {
            instance = new MapConverter();
        }
        return instance;
    }

    public GameStarted convert(Tile[][] map){
        ArrayList<BoardElement> maplist = new ArrayList<BoardElement>();
        int position = 1;
        for(int i = 0; i < (mapWidth); i++){
            for(int j = 0; j < (mapLength); j++){
                BoardElement temp = new BoardElement();
                for (Attribute a : map[i][j].getAttributes()) {
                    temp.addAttribute(a);
                }
                temp.setPosition(position);
                maplist.add(temp);
                position = position + 1;
            }
        }
        return new GameStarted(maplist);
    }

    public Tile[][] reconvert(GameStarted body){
        int index1;
        int index2;
        Tile[][] map = new Tile[mapWidth][mapLength];
        ArrayList<BoardElement> JsonMap = body.getMap();
        for (BoardElement e : JsonMap){
            index1 = calculateFirstIndex(e.getPosition());
            index2 = calculateSecondIndex(e.getPosition());
            Tile temp = new Tile();
            for (Attribute a : e.getField()){
                temp.addAttribute(a);
            }
            map[index1][index2] = temp;

        }

        return map;
    }

    public int calculateFirstIndex(int position){
        int n = position/mapLength;
        if(position%mapLength == 0 && n != 0){
            n = n - 1;
        }
        return n;
    }

    public int calculateSecondIndex(int position){
        int n = position%mapLength;
        if (n == 0){
            n = mapLength;
        }
        n = n - 1;
        return n;
    }



}
