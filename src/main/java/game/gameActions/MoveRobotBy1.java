package game.gameActions;

import game.Player;
import utilities.Utilities.Orientation;

public class MoveRobotBy1 extends Action{


    @Override
    public void doAction(Orientation orientation, Player player) {
        int xCoordinate = player.getRobot().getPosition().getX();
        int yCoordinate = player.getRobot().getPosition().getY();

        switch (orientation) {
            case UP:
                player.getRobot().setPosition(xCoordinate, yCoordinate - 1);
                break;
            case DOWN:
                player.getRobot().setPosition(xCoordinate, yCoordinate + 1);
                break;
            case LEFT:
                player.getRobot().setPosition(xCoordinate + 1, yCoordinate);
                break;
            case RIGHT:
                player.getRobot().setPosition(xCoordinate - 1, yCoordinate);
                break;
        }
    }
}

