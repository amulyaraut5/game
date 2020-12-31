package game.gameObjects.tiles;

import game.Player;
import utilities.Utilities.Orientation;

/**
 * @author Amulya
 */

public class Gear extends Attribute {

    private Orientation orientation; //gear rotation: RIGHT = clockwise, LEFT = Anti-clockwise

    public Gear(Orientation orientation) {
        this.orientation = orientation;
        this.type = "Gear";
    }

    /**
     * Rotates the robot in anti-clockwise direction and clock wise direction.
     * Orientation is initialized by constructor.
     *
     * @param player
     */

    @Override
    public void performAction(Player player) {

        switch (orientation) {
            case RIGHT:
                switch (player.getRobot().getOrientation()) {
                    case UP: //NORTH
                        player.getRobot().setOrientation(Orientation.LEFT);
                        break;
                    case LEFT: //EAST
                        player.getRobot().setOrientation(Orientation.DOWN);
                        break;
                    case DOWN: //SOUTH
                        player.getRobot().setOrientation(Orientation.RIGHT);
                        break;
                    case RIGHT: //WEST
                        player.getRobot().setOrientation(Orientation.UP);
                }
                break;
            case LEFT:
                switch (player.getRobot().getOrientation()) {
                    case UP: //NORTH
                        player.getRobot().setOrientation(Orientation.RIGHT);
                        break;
                    case LEFT: //EAST
                        player.getRobot().setOrientation(Orientation.DOWN);
                        break;
                    case DOWN: //SOUTH
                        player.getRobot().setOrientation(Orientation.LEFT);
                        break;
                    case RIGHT: //WEST
                        player.getRobot().setOrientation(Orientation.UP);
                        break;
                }
                break;
        }
    }

}