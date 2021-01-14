package game.gameObjects.maps;

import utilities.Utilities;

public class RiskyCrossing extends Blueprint {
    public RiskyCrossing(){
        this.name = "Risky Crossing";
        this.gameLength = "short";
        this.difficulty = Utilities.Difficulty.BEGINNER;
        this.minPlayers = 2;
        this.maxPlayers = 6;


        mapBlueprint = new Object[][]{
                {00, 22, 00, 00, 00, 00, 00, 20, 00, 00},
                {31, 22, 00,121, 13,132, 00,201, 23, 23},
                {23,230, 00, 12, 00,101,132, 00, 00, 00},
                {00, 00, 00, 12, 71, 00,101, 13,132, 00},
                {00,121, 13,130, 00, 00, 00, 32, 10, 00},
                {00, 12, 71, 00, 00, 00,112, 11,103, 00},
                {00,110, 11,123, 00, 71, 10, 00, 00, 00},
                {00, 00, 00,110,123, 00, 10, 00,212, 21},
                {21, 21,223, 00,110, 11,103, 00, 20, 00},
                {00, 00, 22, 00, 00, 00, 00, 00, 20, 00},
        };
    }
}