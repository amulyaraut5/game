package game.gameObjects.maps;

import game.gameObjects.tiles.Tile;
import utilities.Coordinate;
import utilities.Utilities;

import java.util.ArrayList;

public abstract class Map {

    protected String name;
    protected String gameLength;
    protected int minPlayers;
    protected int maxPlayers;
    protected Utilities.Difficulty difficulty;
    protected ArrayList<Coordinate> startPositions;
    protected int[][] mapBlueprint;
    protected int width;
    protected int length;

    private static ArrayList<Tile> finalMap;


    Map() {
        this.finalMap = new ArrayList<Tile>();

    }


    public static void generateMap(Map map) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                finalMap.add(Tile.getInstance().createTile(map.mapBlueprint[i][j]));
                System.out.println(j);
                System.out.println(i);
            }
        }
    }

    public static ArrayList<Tile> getMap() {
        return finalMap;
    }
}