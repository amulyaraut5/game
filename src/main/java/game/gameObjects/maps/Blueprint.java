package game.gameObjects.maps;

import utilities.Utilities.Difficulty;

public abstract class Blueprint {

    protected String name;
    protected String gameLength;
    protected int minPlayers;
    protected int maxPlayers;
    protected Difficulty difficulty;
    protected Object[][] mapBlueprint;
    protected int width;
    protected int height;

    public Object[][] getMapBlueprint() {
        return mapBlueprint;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

