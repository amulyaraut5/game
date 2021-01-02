package game.gameActions;

import game.Player;
import utilities.Utilities.Orientation;

public class RotateRobot extends Action{


    @Override
    public void doAction(Orientation orientation, Player player) {
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
