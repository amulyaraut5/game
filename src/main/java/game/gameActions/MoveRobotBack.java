package game.gameActions;

import game.Player;
import utilities.Utilities.Orientation;

/**
 * Moves the robot one space back. The robot does not change the direction it is facing.
 * @author annika
 */
public class MoveRobotBack extends Action{
    @Override
    public void doAction(Orientation orientation, Player player) {
        player.setDirection(player.getDirection().getOpposite());
        new MoveRobot().doAction(orientation, player);
    }
}
