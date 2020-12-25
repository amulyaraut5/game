package game.gameObjects.maps;

import game.gameObjects.Coordinate;
import utilities.Utilities;

import java.util.ArrayList;

public abstract class Map {

    protected String name;
    protected String gameLength;
    protected int minPlayers;
    protected int maxPlayers;
    protected Utilities.Difficulty difficulty;

    protected ArrayList<Coordinate> startPositions;
    protected int[][] MapBlueprint;

}