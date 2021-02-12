package game.gameObjects.maps;

public class StartZone extends Blueprint {

    public StartZone() {
        name = "Start Zone";

        mapBlueprint = new Object[][]{
                { 0,  0, 11},
                { 0,  4,  0},
                { 0, 90,  0},
                { 4,  0,  0},
                { 1,  4, 91},
                { 0,  4, 91},
                { 4,  0,  0},
                { 0, 92,  0},
                { 0,  4,  0},
                { 0,  0, 11},
        };
    }
}
