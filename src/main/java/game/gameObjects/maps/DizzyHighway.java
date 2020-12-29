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
                {00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
                {00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
                {00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
                {00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
                {00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
                {00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
                {00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
                {00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
                {00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
                {00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
        };
    }

}