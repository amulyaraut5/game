package game.gameActions;

import game.Player;
import utilities.Orientation;

/**
 * The RotateRobot class contains only overriden method that rotates the robot either clockwise
 * or antoclockwise based on the orientation.
 * Orientation RIGHT: Clockwise Rotation  & LEFT: Anticlockwise Rotation
 * @author Amulya
 */

public class RotateRobot extends Action{

    private Orientation orientation;

    public RotateRobot(Orientation rotation){
        this.orientation = rotation;
    }

    @Override
    public void doAction(Orientation orientation, Player player) {
        switch (orientation) {
            case RIGHT:
                switch (player.getRobot().getOrientation()) {
                    case UP -> player.getRobot().setOrientation(Orientation.RIGHT);
                    case RIGHT -> player.getRobot().setOrientation(Orientation.DOWN);
                    case DOWN -> player.getRobot().setOrientation(Orientation.LEFT);
                    case LEFT -> player.getRobot().setOrientation(Orientation.UP);
                }
                break;
            case LEFT:
                switch (player.getRobot().getOrientation()) {
                    case UP -> player.getRobot().setOrientation(Orientation.LEFT);
                    case LEFT -> player.getRobot().setOrientation(Orientation.DOWN);
                    case DOWN -> player.getRobot().setOrientation(Orientation.RIGHT);
                    case RIGHT -> player.getRobot().setOrientation(Orientation.UP);
                }
                break;
        }
    }
}
