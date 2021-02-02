package game.gameActions;

import game.Player;
import utilities.enums.Orientation;

/**
 * The MoveRobot class contains only overriden method that moves the Robot one space
 * in any direction but based on the orienation.
 * The orientation of the Robot remains unchanged.
 * It is not checked whether there is wall or not.
 * @author Amulya
 */

public class MoveRobot extends Action{

    @Override
    public void doAction(Orientation orientation, Player player) {
        int xCoordinate = player.getRobot().getCoordinate().getX();
        int yCoordinate = player.getRobot().getCoordinate().getY();

        /*
        switch (orientation) {
            case UP -> player.getRobot().setPosition(xCoordinate, yCoordinate - 1);
            case DOWN -> player.getRobot().setPosition(xCoordinate, yCoordinate + 1);
            case LEFT ->  player.getRobot().setPosition(xCoordinate + 1, yCoordinate);
            case RIGHT -> player.getRobot().setPosition(xCoordinate - 1, yCoordinate);
        }

         */
    }
}

