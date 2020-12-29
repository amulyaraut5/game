package game.gameObjects.maps;


import utilities.Utilities;

public class DizzyHighway extends Map {


    public DizzyHighway() {
        this.name = "Dizzy Highway";
        this.difficulty = Utilities.Difficulty.BEGINNER;
        this.minPlayers = 2;
        this.maxPlayers = 4;

        MapBlueprint = new int[][] { {//TODO MAP
             } };
    }
}