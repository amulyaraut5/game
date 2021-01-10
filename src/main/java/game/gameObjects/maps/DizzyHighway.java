package game.gameObjects.maps;


import utilities.Utilities;

public class DizzyHighway extends Blueprint {


    public DizzyHighway() {
        this.name = "Dizzy Highway";
        this.gameLength = "short";
        this.difficulty = Utilities.Difficulty.BEGINNER;
        this.minPlayers = 2;
        this.maxPlayers = 6;
        this.width = 10;
        this.height = 10;

        //Tiles with multiple attributes (Laser and Wall)
        //outsourced for enhanced clarity in Blueprint
        int[] a = {601, 92};
        int[] b = {611, 93};
        int[] c = {621, 90};
        int[] d = {631, 91};

        mapBlueprint = new Object[][]{
                {00, 22, 22, 00, 00, 00, 00, 00, 00, 71},
                {00,225,234, 23, 23, 23, 23, 23,236, 23},
                {00, 22, 71, 00, 00, 00, 00, 00,205, 23},
                {00, 22, 00, 90, 03,  b, 91, 00, 20, 31},
                {00, 22, 00,  a, 00, 71, 00, 00, 20, 00},
                {00, 22, 00, 00, 71, 00,  c, 00, 20, 00},
                {00, 22, 00, 93,  d, 00, 92, 00, 20, 00},
                {21,227, 00, 00, 00, 00, 00, 71, 20, 00},
                {21,214, 21, 21, 21, 21, 21,216,207, 00},
                {71, 00, 00, 00, 00, 00, 00, 20, 20, 00},
        };
    }


}