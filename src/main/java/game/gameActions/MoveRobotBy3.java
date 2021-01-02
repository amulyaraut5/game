package game.gameActions;

import game.Player;
import utilities.Utilities;

public class MoveRobotBy3 extends Action{

    @Override
    public void doAction(Utilities.Orientation orientation, Player player) {
        int xCoordinate = player.getRobot().getPosition().getX();
        int yCoordinate = player.getRobot().getPosition().getY();

        switch (orientation) {
            case UP:
                player.getRobot().setPosition(xCoordinate, yCoordinate - 3);
                break;
            case DOWN:
                player.getRobot().setPosition(xCoordinate, yCoordinate + 3);
                break;
            case LEFT:
                player.getRobot().setPosition(xCoordinate + 3, yCoordinate);
                break;
            case RIGHT:
                player.getRobot().setPosition(xCoordinate - 3, yCoordinate);
                break;
        }
    }
}
