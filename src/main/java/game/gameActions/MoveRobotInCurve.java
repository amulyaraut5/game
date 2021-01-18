package game.gameActions;

import game.Player;
import utilities.Utilities;
import utilities.enums.Orientation;

/**
 * The MoveRobotInCurve class contains only overriden method that moves the robot in a non-linear fashion
 * based on the orientation of the belt.
 * The orientation of the Robot remains unchanged.
 *
 * @author Amulya
 */

public class MoveRobotInCurve extends Action{
    @Override
    public void doAction(Orientation orientation, Player player) { }

    // For Blue Rotating Belt
    // Todo Split cases with boolean isCrossing
    public void doAction2(Orientation[] orientations,Player player){

        int xCoordinate = player.getRobot().getPosition().getX();
        int yCoordinate = player.getRobot().getPosition().getY();

        if (orientations == Utilities.RIGHT_UP) {
            player.getRobot().setPosition(xCoordinate-1, yCoordinate +1);
        }

        else if (orientations == Utilities.RIGHT_DOWN){
            player.getRobot().setPosition(xCoordinate+1, yCoordinate +1);
        }

        else if (orientations == Utilities.LEFT_UP) {
            player.getRobot().setPosition(xCoordinate-1, yCoordinate -1);
        }

        else if (orientations == Utilities.LEFT_DOWN){
            player.getRobot().setPosition(xCoordinate+1, yCoordinate -1);
        }

        else if(orientations == Utilities.UP_RIGHT){
            player.getRobot().setPosition(xCoordinate-1, yCoordinate +1);
        }

        else if(orientations == Utilities.UP_LEFT){
            player.getRobot().setPosition(xCoordinate-1, yCoordinate -1);
        }

        else if(orientations == Utilities.DOWN_RIGHT){
            player.getRobot().setPosition(xCoordinate+1, yCoordinate +1);
        }

        else if(orientations == Utilities.DOWN_LEFT){
            player.getRobot().setPosition(xCoordinate+1, yCoordinate -1);
        }
    }
}
