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


        mapBlueprint = new int[][]{
                {00, 22, 22, 00, 00, 00, 00, 00, 00, 71},
                {00,225,234, 23, 23, 23, 23, 23,236, 23},
                {00, 22, 71, 00, 00, 00, 00, 00,205, 23},
                {00, 22, 00, 90, 03,611, 91, 00, 20, 31},
                {00, 22, 00,601, 00, 71, 00, 00, 20, 00},
                {00, 22, 00, 00, 71, 00,621, 00, 20, 00},
                {00, 22, 00, 93,631, 00, 92, 00, 20, 00},
                {21,227, 00, 00, 00, 00, 00, 71, 20, 00},
                {21,214, 21, 21, 21, 21, 21,216,207, 00},
                {71, 00, 00, 00, 00, 00, 00, 20, 20, 00},
        };
    }


}