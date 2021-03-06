package game.gameObjects.tiles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Constants;
import utilities.enums.Orientation;
import utilities.enums.Rotation;

/**
 * This  class creates the different tiles.
 *
 * @author Amulya, Simon, annika
 */
public final class TileFactory {
    private static final Logger logger = LogManager.getLogger();

    private TileFactory() {
    }

    /**
     * This method creates the tile with specific attribute.
     * Depending upon the needs of map, we can create our own tile with
     * multiple attributes with different orientations.
     *
     * @param
     * @return tile
     */
    public static Tile createTile(Object tileID) {
        Tile tile = new Tile();

        //Check if tileID is just one ID or a list of IDs (for tiles with multiple Attributes)
        if (tileID instanceof int[]) {
            int[] idList = (int[]) tileID;

            for (int j : idList) {
                tile.addAttribute(convertIdToAttribute(j));
            }
        } else if (tileID instanceof Integer) {
            int attributeID = (int) tileID;
            tile.addAttribute(convertIdToAttribute(attributeID));
        } else
            throw new IllegalArgumentException(tileID.getClass() + " is not allowed as an ID. Use only int or int[].");
        return tile;
    }

    /**
     * Every integer represents a certain attribute.
     * Every Attribute has it's own specific id which can be used to create the tile.
     *
     * @param attributeID
     * @return
     */
    private static Attribute convertIdToAttribute(int attributeID) {
        Attribute attribute;

        switch (attributeID) {
            case 0 -> attribute = new Empty();
            case 1 -> attribute = new Antenna();
            case 2 -> attribute = new Pit();
            case 3 -> attribute = new RestartPoint(Orientation.DOWN);
            case 5 -> attribute = new RestartPoint(Orientation.RIGHT);
            case 4 -> attribute = new StartPoint();

            //Belt
            case 10 -> attribute = new Belt(Orientation.UP, 1);
            case 11 -> attribute = new Belt(Orientation.RIGHT, 1);
            case 12 -> attribute = new Belt(Orientation.DOWN, 1);
            case 13 -> attribute = new Belt(Orientation.LEFT, 1);

            case 20 -> attribute = new Belt(Orientation.UP, 2);
            case 21 -> attribute = new Belt(Orientation.RIGHT, 2);
            case 22 -> attribute = new Belt(Orientation.DOWN, 2);
            case 23 -> attribute = new Belt(Orientation.LEFT, 2);

            //RotatingBelt
            case 101 -> attribute = new RotatingBelt(Constants.UP_RIGHT, false, 1);
            case 103 -> attribute = new RotatingBelt(Constants.UP_LEFT, false, 1);
            case 110 -> attribute = new RotatingBelt(Constants.RIGHT_UP, false, 1);
            case 112 -> attribute = new RotatingBelt(Constants.RIGHT_DOWN, false, 1);
            case 121 -> attribute = new RotatingBelt(Constants.DOWN_RIGHT, false, 1);
            case 123 -> attribute = new RotatingBelt(Constants.DOWN_LEFT, false, 1);
            case 132 -> attribute = new RotatingBelt(Constants.LEFT_DOWN, false, 1);
            case 130 -> attribute = new RotatingBelt(Constants.LEFT_UP, false, 1);

            case 105 -> attribute = new RotatingBelt(Constants.UP_RIGHT, true, 1);
            case 107 -> attribute = new RotatingBelt(Constants.UP_LEFT, true, 1);
            case 114 -> attribute = new RotatingBelt(Constants.RIGHT_UP, true, 1);
            case 116 -> attribute = new RotatingBelt(Constants.RIGHT_DOWN, true, 1);
            case 125 -> attribute = new RotatingBelt(Constants.DOWN_RIGHT, true, 1);
            case 127 -> attribute = new RotatingBelt(Constants.DOWN_LEFT, true, 1);
            case 136 -> attribute = new RotatingBelt(Constants.LEFT_DOWN, true, 1);
            case 134 -> attribute = new RotatingBelt(Constants.LEFT_UP, true, 1);

            case 201 -> attribute = new RotatingBelt(Constants.UP_RIGHT, false, 2);
            case 203 -> attribute = new RotatingBelt(Constants.UP_LEFT, false, 2);
            case 210 -> attribute = new RotatingBelt(Constants.RIGHT_UP, false, 2);
            case 212 -> attribute = new RotatingBelt(Constants.RIGHT_DOWN, false, 2);
            case 221 -> attribute = new RotatingBelt(Constants.DOWN_RIGHT, false, 2);
            case 223 -> attribute = new RotatingBelt(Constants.DOWN_LEFT, false, 2);
            case 232 -> attribute = new RotatingBelt(Constants.LEFT_DOWN, false, 2);
            case 230 -> attribute = new RotatingBelt(Constants.LEFT_UP, false, 2);

            case 205 -> attribute = new RotatingBelt(Constants.UP_RIGHT, true, 2);
            case 207 -> attribute = new RotatingBelt(Constants.UP_LEFT, true, 2);
            case 214 -> attribute = new RotatingBelt(Constants.RIGHT_UP, true, 2);
            case 216 -> attribute = new RotatingBelt(Constants.RIGHT_DOWN, true, 2);
            case 225 -> attribute = new RotatingBelt(Constants.DOWN_RIGHT, true, 2);
            case 227 -> attribute = new RotatingBelt(Constants.DOWN_LEFT, true, 2);
            case 236 -> attribute = new RotatingBelt(Constants.LEFT_DOWN, true, 2);
            case 234 -> attribute = new RotatingBelt(Constants.LEFT_UP, true, 2);

            //ControlPoint
            case 31 -> attribute = new ControlPoint(1);
            case 32 -> attribute = new ControlPoint(2);
            case 33 -> attribute = new ControlPoint(3);
            case 34 -> attribute = new ControlPoint(4);
            case 35 -> attribute = new ControlPoint(5);
            case 36 -> attribute = new ControlPoint(6);
            case 37 -> attribute = new ControlPoint(7);
            case 38 -> attribute = new ControlPoint(8);
            case 39 -> attribute = new ControlPoint(9);

            //Gear
            case 41 -> attribute = new Gear(Rotation.RIGHT);
            case 42 -> attribute = new Gear(Rotation.LEFT);

            //PushPanel
            case 501 -> attribute = new PushPanel(Orientation.UP, new int[]{1});
            case 502 -> attribute = new PushPanel(Orientation.UP, new int[]{2});
            case 503 -> attribute = new PushPanel(Orientation.UP, new int[]{3});
            case 504 -> attribute = new PushPanel(Orientation.UP, new int[]{4});
            case 505 -> attribute = new PushPanel(Orientation.UP, new int[]{5});
            case 506 -> attribute = new PushPanel(Orientation.UP, new int[]{2, 4});
            case 507 -> attribute = new PushPanel(Orientation.UP, new int[]{1, 3, 5});

            case 511 -> attribute = new PushPanel(Orientation.RIGHT, new int[]{1});
            case 512 -> attribute = new PushPanel(Orientation.RIGHT, new int[]{2});
            case 513 -> attribute = new PushPanel(Orientation.RIGHT, new int[]{3});
            case 514 -> attribute = new PushPanel(Orientation.RIGHT, new int[]{4});
            case 515 -> attribute = new PushPanel(Orientation.RIGHT, new int[]{5});
            case 516 -> attribute = new PushPanel(Orientation.RIGHT, new int[]{2, 4});
            case 517 -> attribute = new PushPanel(Orientation.RIGHT, new int[]{1, 3, 5});

            case 521 -> attribute = new PushPanel(Orientation.DOWN, new int[]{1});
            case 522 -> attribute = new PushPanel(Orientation.DOWN, new int[]{2});
            case 523 -> attribute = new PushPanel(Orientation.DOWN, new int[]{3});
            case 524 -> attribute = new PushPanel(Orientation.DOWN, new int[]{4});
            case 525 -> attribute = new PushPanel(Orientation.DOWN, new int[]{5});
            case 526 -> attribute = new PushPanel(Orientation.DOWN, new int[]{2, 4});
            case 527 -> attribute = new PushPanel(Orientation.DOWN, new int[]{1, 3, 5});

            case 531 -> attribute = new PushPanel(Orientation.LEFT, new int[]{1});
            case 532 -> attribute = new PushPanel(Orientation.LEFT, new int[]{2});
            case 533 -> attribute = new PushPanel(Orientation.LEFT, new int[]{3});
            case 534 -> attribute = new PushPanel(Orientation.LEFT, new int[]{4});
            case 535 -> attribute = new PushPanel(Orientation.LEFT, new int[]{5});
            case 536 -> attribute = new PushPanel(Orientation.LEFT, new int[]{2, 4});
            case 537 -> attribute = new PushPanel(Orientation.LEFT, new int[]{1, 3, 5});

            //Laser
            case 601 -> attribute = new Laser(Orientation.UP, 1);
            case 602 -> attribute = new Laser(Orientation.UP, 2);
            case 603 -> attribute = new Laser(Orientation.UP, 3);

            case 611 -> attribute = new Laser(Orientation.RIGHT, 1);
            case 612 -> attribute = new Laser(Orientation.RIGHT, 2);
            case 613 -> attribute = new Laser(Orientation.RIGHT, 3);

            case 621 -> attribute = new Laser(Orientation.DOWN, 1);
            case 622 -> attribute = new Laser(Orientation.DOWN, 2);
            case 623 -> attribute = new Laser(Orientation.DOWN, 3);

            case 631 -> attribute = new Laser(Orientation.LEFT, 1);
            case 632 -> attribute = new Laser(Orientation.LEFT, 2);
            case 633 -> attribute = new Laser(Orientation.LEFT, 3);

            //EnergySpace
            case 70 -> attribute = new EnergySpace(0);
            case 71 -> attribute = new EnergySpace(1);
            case 72 -> attribute = new EnergySpace(2);
            case 73 -> attribute = new EnergySpace(3);
            case 74 -> attribute = new EnergySpace(4);
            case 75 -> attribute = new EnergySpace(5);
            case 76 -> attribute = new EnergySpace(6);
            case 77 -> attribute = new EnergySpace(7);
            case 78 -> attribute = new EnergySpace(8);
            case 79 -> attribute = new EnergySpace(9);

            //Wall
            case 90 -> attribute = new Wall(Orientation.UP);
            case 91 -> attribute = new Wall(Orientation.RIGHT);
            case 92 -> attribute = new Wall(Orientation.DOWN);
            case 93 -> attribute = new Wall(Orientation.LEFT);

            case 901 -> attribute = new Wall(Constants.UP_RIGHT);
            case 910 -> attribute = new Wall(Constants.RIGHT_DOWN);
            case 923 -> attribute = new Wall(Constants.DOWN_LEFT);
            case 930 -> attribute = new Wall(Constants.LEFT_UP);

            case 902 -> attribute = new Wall(Constants.UP_DOWN);
            case 920 -> attribute = new Wall(Constants.DOWN_UP);
            case 913 -> attribute = new Wall(Constants.RIGHT_LEFT);
            case 931 -> attribute = new Wall(Constants.LEFT_RIGHT);

            default -> {
                logger.error("ID " + attributeID + " could not be converted! " + attributeID + " is no known Tile.");
                attribute = new Empty();
            }
        }
        return attribute;
    }
}
