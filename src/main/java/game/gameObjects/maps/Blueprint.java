package game.gameObjects.maps;

import utilities.Utilities.Difficulty;

public abstract class Blueprint {

    protected String name;
    protected String gameLength;
    protected int minPlayers;
    protected int maxPlayers;
    protected Difficulty difficulty;
    protected Object[][] mapBlueprint;

    public Object[][] getMapBlueprint() {
        return mapBlueprint;
    }
}

