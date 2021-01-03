package game.gameObjects.tiles;

import utilities.Utilities;
import utilities.Utilities.Orientation;

import java.util.ArrayList;

public class Tile {

    private static Tile instance;
    protected String imagePath;
    /**
     * saves one or multiple Attributes for one tile
     */
    private ArrayList<Attribute> attributes = new ArrayList<>();
    private Attribute attribute;

    /**
     * Constructor for tiles.
     */
    private Tile() {

    }

    //private Image image;

    public static Tile getInstance() {
        if (instance == null) {
            instance = new Tile();
        }
        return instance;
    }


    public Attribute getAttribute() {
        return attributes.get(0);
    }

    /**
     * Adds an Attribute to a tile by saving it in the ArrayList of attributes
     *
     * @param attribute added Attribute
     */
    public void addAttribute(Attribute attribute) {
        attributes.add(attribute);
    }

    /**
     * It sets the priority order of execution of different attributes of tiles.
     */
    public void priorityOrder() {
        //TODO
    }


    /**
     * This method creates the tile with specific attribute.
     * Every tile has then it's own specific id which can be called while laying out the map structure.
     * Depending upon the needs of map, we can create our own tile with multiple attributes with different orientations.
     *
     * @param
     * @return tile
     */


    public Tile createTile(int tileID) {
        Tile tile = new Tile();
        Attribute attribute = null;

        switch (tileID) {
            case 00 -> attribute = new Empty();
            case 111 -> attribute = new Antenna();

            //Belt
            case 01 -> attribute = new Belt(Orientation.UP, 1);
            case 02 -> attribute = new Belt(Orientation.DOWN, 1);
            case 03 -> attribute = new Belt(Orientation.LEFT, 1);
            case 04 -> attribute = new Belt(Orientation.RIGHT, 1);
            case 11 -> attribute = new Belt(Orientation.UP, 2);
            case 12 -> attribute = new Belt(Orientation.DOWN, 2);
            case 13 -> attribute = new Belt(Orientation.LEFT, 2);
            case 14 -> attribute = new Belt(Orientation.RIGHT, 2);

            //RotatingBelt
            case 21 -> attribute = new RotatingBelt(Utilities.UP_LEFT, false, 1);
            case 22 -> attribute = new RotatingBelt(Utilities.UP_RIGHT, false, 1);
            case 23 -> attribute = new RotatingBelt(Utilities.DOWN_LEFT, false, 1);
            case 24 -> attribute = new RotatingBelt(Utilities.DOWN_RIGHT, false, 1);

            case 25 -> attribute = new RotatingBelt(Utilities.UP_LEFT, true, 1);
            case 26 -> attribute = new RotatingBelt(Utilities.UP_RIGHT, true, 1);
            case 27 -> attribute = new RotatingBelt(Utilities.DOWN_LEFT, true, 1);
            case 28 -> attribute = new RotatingBelt(Utilities.DOWN_RIGHT, true, 1);

            case 31 -> attribute = new RotatingBelt(Utilities.UP_LEFT, false, 2);
            case 32 -> attribute = new RotatingBelt(Utilities.UP_RIGHT, false, 2);
            case 33 -> attribute = new RotatingBelt(Utilities.DOWN_LEFT, false, 2);
            case 34 -> attribute = new RotatingBelt(Utilities.DOWN_RIGHT, false, 2);

            case 101 -> attribute = new RotatingBelt(Utilities.LEFT_UP, false, 2);
            case 102 -> attribute = new RotatingBelt(Utilities.RIGHT_UP, false, 2);
            case 103 -> attribute = new RotatingBelt(Utilities.LEFT_DOWN, false, 2);
            case 104 -> attribute = new RotatingBelt(Utilities.RIGHT_DOWN, false, 2);

            case 35 -> attribute = new RotatingBelt(Utilities.UP_LEFT, true, 2);
            case 36 -> attribute = new RotatingBelt(Utilities.UP_RIGHT, true, 2);
            case 37 -> attribute = new RotatingBelt(Utilities.DOWN_LEFT, true, 2);
            case 38 -> attribute = new RotatingBelt(Utilities.DOWN_RIGHT, true, 2);

            case 105 -> attribute = new RotatingBelt(Utilities.LEFT_UP, true, 2);
            case 106 -> attribute = new RotatingBelt(Utilities.RIGHT_UP, true, 2);
            case 107 -> attribute = new RotatingBelt(Utilities.LEFT_DOWN, true, 2);
            case 108 -> attribute = new RotatingBelt(Utilities.RIGHT_DOWN, true, 2);

            //PushPanel
            case 40 -> attribute = new PushPanel(Orientation.DOWN, new int[]{2, 4});
            case 41 -> attribute = new PushPanel(Orientation.UP, new int[]{2, 4});
            case 42 -> attribute = new PushPanel(Orientation.LEFT, new int[]{2, 4});
            case 43 -> attribute = new PushPanel(Orientation.RIGHT, new int[]{2, 4});
            case 44 -> attribute = new PushPanel(Orientation.DOWN, new int[]{1, 3, 5});
            case 45 -> attribute = new PushPanel(Orientation.UP, new int[]{1, 3, 5});
            case 46 -> attribute = new PushPanel(Orientation.LEFT, new int[]{1, 3, 5});
            case 47 -> attribute = new PushPanel(Orientation.RIGHT, new int[]{1, 3, 5});

            //Gear
            case 51 -> attribute = new Gear(Orientation.LEFT);
            case 52 -> attribute = new Gear(Orientation.RIGHT);

            //Laser
            case 61 -> attribute = new Laser(Orientation.DOWN, 1);
            case 62 -> attribute = new Laser(Orientation.UP, 1);
            case 63 -> attribute = new Laser(Orientation.LEFT, 1);
            case 64 -> attribute = new Laser(Orientation.RIGHT, 1);

            case 65 -> attribute = new Laser(Orientation.DOWN, 2);
            case 66 -> attribute = new Laser(Orientation.UP, 2);
            case 67 -> attribute = new Laser(Orientation.LEFT, 2);
            case 68 -> attribute = new Laser(Orientation.RIGHT, 2);

            case 71 -> attribute = new Laser(Orientation.DOWN, 3);
            case 72 -> attribute = new Laser(Orientation.UP, 3);
            case 73 -> attribute = new Laser(Orientation.LEFT, 3);
            case 74 -> attribute = new Laser(Orientation.RIGHT, 3);

            //EnergySpace
            case 81 -> attribute = new EnergySpace(0);
            case 82 -> attribute = new EnergySpace(1);

            //Pit
            case 90 -> attribute = new Pit();

            //Wall
            case 91 -> attribute = new Wall(Orientation.DOWN);
            case 92 -> attribute = new Wall(Orientation.UP);
            case 93 -> attribute = new Wall(Orientation.LEFT);
            case 94 -> attribute = new Wall(Orientation.RIGHT);
            case 95 -> attribute = new Wall(Utilities.UP_LEFT);
            case 96 -> attribute = new Wall(Utilities.UP_RIGHT);
            case 97 -> attribute = new Wall(Utilities.DOWN_LEFT);
            case 98 -> attribute = new Wall(Utilities.DOWN_RIGHT);
        }

        tile.addAttribute(attribute);
        return tile;
    }

    public boolean isTileOccupied() {
        return true;
    }
}