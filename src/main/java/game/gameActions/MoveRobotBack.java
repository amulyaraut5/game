package game.gameActions;

import game.Player;
import game.gameObjects.robot.Robot;
import utilities.Orientation;

/**
 * Moves the robot one space back. The robot does not change the direction it is facing.
 *
 * @author annika
 */
public class MoveRobotBack extends Action {
    @Override
    public void doAction(Orientation orientation, Player player) {
        Robot robot = player.getRobot();
        robot.setOrientation(robot.getOrientation().getOpposite());
        new MoveRobot().doAction(orientation, player);
    }
}
