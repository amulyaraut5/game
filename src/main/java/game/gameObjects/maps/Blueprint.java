package game.gameObjects.maps;

public abstract class Blueprint {

    protected String name;
    protected Object[][] mapBlueprint;

    public Object[][] getMapBlueprint() {
        return mapBlueprint;
    }
}

