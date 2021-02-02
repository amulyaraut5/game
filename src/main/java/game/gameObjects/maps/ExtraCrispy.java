package game.gameObjects.maps;

public class ExtraCrispy extends Blueprint {

    public ExtraCrispy() {
        name = "Extra Crispy";

        //Tiles with multiple attributes (Laser and Wall)
        //outsourced for enhanced clarity in Blueprint
        int[] a = {31, 621, 90}; //checkpoint 1, laser, wall
        int[] b = {32, 601, 92}; //checkpoint 2, laser, wall
        int[] c = {33, 601, 92}; //checkpoint 3, laser, wall
        int[] d = {34, 621, 90}; //checkpoint 4, laser, wall
        int[] e = {611, 93}; //laser, wall
        int[] f = {631, 91}; //laser, wall
        int[] g = {71, 90}; //energy, wall


        mapBlueprint = new Object[][]{
                { 0, 22,  0,  0,  0, 71,  e,  0,  f,  0},
                { 0, 22,  0,112, 13,  0,  0,  0, 22,  0},
                { 0, 22,  d,  2,  0,  0,  2,  a, 22,  0},
                { 0,201, 21,  2,  e,  f,  2, 23,230,  0},
                { g,  0,  0, 41,  0,  0, 91,  0, 71, 90},
                {92,  0,  0, 93, 71,  0, 42,  0,  0, 92},
                { 0,212, 21,  2,  e,  f,  2, 23,232,  0},
                { 0, 20,  b,  2,  0,  0,  2,  c, 20,  0},
                { 0, 20,  0,  0,  0, 11,103,  0, 20,  0},
                {71,  e,  0,  f,  0,  0,  0,  0, 20,  0},
        };
    }
}