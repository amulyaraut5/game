package game.gameActions;

import game.Player;
import utilities.Utilities.Orientation;

public class RotateRobot extends Action{

    private Orientation orientation;

    public RotateRobot(Orientation orientation){
        this.orientation = orientation;
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
