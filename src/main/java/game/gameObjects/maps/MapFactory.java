package game.gameObjects.maps;

import java.util.ArrayList;

public class MapFactory {
    private static MapFactory instance;
    private ArrayList<Map> maps;

    private MapFactory() {
        generateMaps();
    }

    public static MapFactory getInstance(){
        if (instance == null) {
            instance = new MapFactory();
        }
        return instance;
    }

    /**
     * Adds the maps of the game to a ArrayList
     */
    private void generateMaps() {
        maps = new ArrayList<>();
        maps.add(new DizzyHighway());
    }

    /**
     * returns every map in the game
     * @return maps list containing every map in the game
     */
    public ArrayList<Map> getMaps(){
        return maps;
    }

    /**
     * returns the map with the matching id
     * @param id the id of the map to request
     * @return the requested map
     */
    public Map getMap(int id) {
        return maps.get(id);
    }
}
