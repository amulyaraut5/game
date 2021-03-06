package game.gameObjects.maps;

public class RiskyCrossing extends Blueprint {
    public RiskyCrossing() {
        name = "Risky Crossing";

        mapBlueprint = new Object[][]{
                { 0, 22,  0,  0,  0,  0,  0, 20,  0,  0},
                {31, 22,  0,121, 13,132,  0,201, 23, 23},
                {23,230,  0, 12,  0,101,132,  0,  0,  0},
                { 0,  0,  0, 12, 71,  0,101, 13,132,  0},
                { 0,121, 13,130,  0,  0,  0, 32, 10,  0},
                { 0, 12, 71,  0,  0,  0,112, 11,103,  0},
                { 0,110, 11,123,  0, 71, 10,  0,  0,  0},
                { 0,  0,  0,110,123,  0, 10,  0,212, 21},
                {21, 21,223,  0,110, 11,103,  0, 20,  0},
                { 0,  0, 22,  0,  0,  0,  0,  0, 20,  0},
        };
    }
}