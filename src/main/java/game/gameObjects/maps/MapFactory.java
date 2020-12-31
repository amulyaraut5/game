package game.gameObjects.maps;

import utilities.JSONProtocol.body.gameStarted.Field;

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

    public Field[][] constructMap(Map map){
        Field[][] finalMap = new Field[map.width-1][map.length-1];
        for(int i = 0; i < (map.width); i++){
            for(int j = 0; j < (map.length); i++){
                contructField(map.mapBlueprint[i][j]);
            }
        }
        return finalMap;
    }

    public Field contructField(int i){
        //TODO Switch case to construct specific tile

        return null;
    }
}
