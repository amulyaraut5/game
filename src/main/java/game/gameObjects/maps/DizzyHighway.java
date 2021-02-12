package game.gameObjects.maps;

public class DizzyHighway extends Blueprint {

    public DizzyHighway() {
        name = "Dizzy Highway";

        //Tiles with multiple attributes (Laser and Wall)
        //outsourced for enhanced clarity in Blueprint
        int[] a = {601, 92};
        int[] b = {611, 93};
        int[] c = {621, 90};
        int[] d = {631, 91};

        mapBlueprint = new Object[][]{
                { 0, 22, 22,  0,  0,  0,  0,  0,  0, 71},
                { 0,225,234, 23, 23, 23, 23, 23,236, 23},
                { 0, 22, 71,  0,  0,  0,  0,  0,205, 23},
                { 0, 22,  0, 90,  3,  b, 91,  0, 20, 31},
                { 0, 22,  0,  a,  0, 71,  0,  0, 20,  0},
                { 0, 22,  0,  0, 71,  0,  c,  0, 20,  0},
                { 0, 22,  0, 93,  d,  0, 92,  0, 20,  0},
                {21,227,  0,  0,  0,  0,  0, 71, 20,  0},
                {21,214, 21, 21, 21, 21, 21,216,207,  0},
                {71,  0,  0,  0,  0,  0,  0, 20, 20,  0},
        };
    }
}