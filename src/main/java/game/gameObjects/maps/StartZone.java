package game.gameObjects.maps;

public class StartZone extends Blueprint {

    public StartZone() {
        name = "Start Zone";

        mapBlueprint = new Object[][]{
                {00, 00, 11},
                {00, 04, 00},
                {00, 90, 00},
                {04, 00, 00},
                {01, 04, 91},
                {00, 04, 91},
                {04, 00, 00},
                {00, 92, 00},
                {00, 04, 00},
                {00, 00, 11},
        };
    }
}
