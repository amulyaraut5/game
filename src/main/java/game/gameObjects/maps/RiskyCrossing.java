package game.gameObjects.maps;

import utilities.Utilities;

public class RiskyCrossing extends Map {
    public RiskyCrossing(){
        this.name = "RiskyCrossing";
        this.gameLength = "short";
        this.difficulty = Utilities.Difficulty.BEGINNER;
        this.minPlayers = 2;
        this.maxPlayers = 6;
        this.width = 10;
        this.length = 10;


        mapBlueprint = new Object[][]{
                {00, 22, 00, 00, 00, 00, 00, 20, 00, 00},
                {31, 22, 00, 121, 13, 132, 00, 201,23, 23},
                {23, 230, 00, 12, 00, 110, 132, 00,00, 00},
                {00, 00, 00, 12, 71,  00, 110, 13, 132, 00},
                {00, 112, 13,103, 00, 00, 00, 32, 10, 00},
                {00, 12, 71, 00, 00, 00,  121, 11, 103, 00},
                {00, 110, 11, 132,  00, 71, 10, 00, 00, 00},
                {00, 00, 00, 110, 132, 00, 10, 00, 212, 21},
                {21, 21, 223, 00, 110, 11, 103,00,20, 00},
                {00, 00, 22, 00, 00, 00, 00, 00, 20, 00},
        };
    }
}