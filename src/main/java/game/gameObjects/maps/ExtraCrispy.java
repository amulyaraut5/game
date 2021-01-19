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
                {00, 22, 00, 00, 00, 71,  e, 00,  f, 00},
                {00, 22, 00,112, 13, 00, 00, 00, 22, 00},
                {00, 22,  d, 02, 00, 00, 02,  a, 22, 00},
                {00,201, 21, 02,  e,  f, 02, 23,230, 00},
                { g, 00, 00, 41, 00, 00, 91, 00, 71, 90},
                {92, 00, 00, 93, 71, 00, 42, 00, 00, 92},
                {00,212, 21, 02,  e,  f, 02, 23,232, 00},
                {00, 20,  b, 02, 00, 00, 02,  c, 20, 00},
                {00, 20, 00, 00, 00, 11,103, 00, 20, 00},
                {71,  e, 00,  f, 00, 00, 00, 00, 20, 00},
        };
    }
}