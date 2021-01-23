package game.gameActions;

import game.Player;
import utilities.JSONProtocol.body.PlayerTurning;
import utilities.enums.Orientation;

/**
 * The RotateRobot class contains only overriden method that rotates the robot either clockwise
 * or antoclockwise based on the orientation.
 * Orientation RIGHT: Clockwise Rotation  & LEFT: Anticlockwise Rotation
 *
 * @author Amulya
 */

public class RotateRobot extends Action {

    private Orientation orientation;

    public RotateRobot(Orientation rotation) {
        this.orientation = rotation;
    }

    @Override
    public void doAction(Orientation orientation, Player player) {
        player.getRobot().rotate(orientation.toRotation());
        server.communicateAll(new PlayerTurning(player.getID(), orientation.toRotation()));
    }
}
