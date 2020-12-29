package game.gameObjects.maps;

import utilities.Position;
import utilities.Utilities;

import java.util.ArrayList;

public abstract class Map {

    protected String name;
    protected String gameLength;
    protected int minPlayers;
    protected int maxPlayers;
    protected Utilities.Difficulty difficulty;

    protected ArrayList<Position> startPositions;
    protected int[][] MapBlueprint;

}