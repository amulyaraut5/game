package game.gameObjects.maps;


import utilities.Utilities;

public class DizzyHighway extends Map {


    public DizzyHighway() {
        this.name = "Dizzy Highway";
        this.gameLength = "short";
        this.difficulty = Utilities.Difficulty.BEGINNER;
        this.minPlayers = 2;
        this.maxPlayers = 6;
        this.width = 10;
        this.length = 10;


        mapBlueprint = new int[][] {
                {00, 12, 12, 00, 00, 00, 00, 00, 00, 82},
                {00, 38,105, 13, 13, 13, 13, 13, 37, 13},
                {00, 12, 82, 00, 00, 00, 00, 00, 36, 13},
                {00, 12, 00, 92, 00, 63, 94, 00, 11, 00},
                {00, 12, 00, 61, 00, 82, 00, 00, 11, 00},
                {00, 12, 00, 00, 82, 00, 62, 00, 11, 00},
                {00, 12, 00, 93, 64, 00, 91, 00, 11, 00},
                {14,107, 00, 00, 00, 00, 00, 82, 11, 00},
                {14,108, 14, 14, 14, 14, 14,106, 35, 00},
                {82, 00, 00, 00, 00, 00, 00, 11, 11, 00},
        };
    }

}