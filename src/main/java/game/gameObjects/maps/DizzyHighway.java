package game.gameObjects.maps;


import utilities.Utilities;

public class DizzyHighway extends Map {


    public DizzyHighway() {
        this.name = "Dizzy Highway";
        this.difficulty = Utilities.Difficulty.BEGINNER;
        this.minPlayers = 2;
        this.maxPlayers = 4;

        MapBlueprint = new int[]{0, 16, 16, 0, 0, 0, 0, 0, 0, 41,
                0, 23, 23, 33, 33, 33, 33, 33, 21, 33,
                0, 16, 41, 0, 0, 0, 0, 0, 21, 33,
        0, 16,0,82,0, };
    }
}