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

    private static ArrayList<Tile> map;

    Map() {
        this.map = new ArrayList<Tile>();
    }

    public void generateMap(int[][] mapBlueprint) {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y<10; y++) {
                map.add(Tile.getInstance().createTile(mapBlueprint[x][y]));
            }
        }
    }

    public static ArrayList<Tile> getMap() {
        return map;
    }
}