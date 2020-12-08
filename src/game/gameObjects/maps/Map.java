package game.gameObjects.maps;

import game.gameObjects.Coordinate;

import java.util.ArrayList;

public abstract class Map {

	private String name;
	private String gameLength;
	private ArrayList<Coordinate> startPositions;
	private int minPlayers;
	private int maxPlayers;
	private int[][] MapBlueprint;

}