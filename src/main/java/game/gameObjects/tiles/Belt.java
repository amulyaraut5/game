package game.gameObjects.tiles;

import game.Player;
import javafx.scene.canvas.GraphicsContext;
import utilities.Coordinate;
import utilities.Utilities.Orientation;

/**
 * @author Amulya
 */

public class Belt extends Attribute {

    private int speed; // 1 = Green Conveyor, 2 = Blue Conveyor
    private Orientation orientation; //direction in which the belt runs

    public Belt(Orientation orientation, int speed) {
        this.speed = speed;
        this.orientation = orientation;
        this.type = "Belt";
    }

    /**
     * The GreenConveyor belt pushes the robot in the direction of tile by
     * one space.
     * Once a robot has moved off a belt, the belt has no longer effect.
     *
     * @param player
     */
    @Override
    public void performAction(Player player) {

        int xCoordinate = player.getRobot().getPosition().getX();
        int yCoordinate = player.getRobot().getPosition().getY();

        if (collisionPointExist()) {
            // No movement
        } else {
            // Need of location of robot and direction of tile.
            updateRobotCoordinates(xCoordinate, yCoordinate, player, this.speed);
        }
    }

    @Override
    public void draw(GraphicsContext gc, Coordinate position) {

    }

    /**
     * This method checks if two robots converge at the same point or not.
     *
     * @return
     */

    private boolean collisionPointExist() {
        //TODO
        return false;
    }

    /**
     * This method relocates the robot to new position based on the speed and
     * direction of conveyor belt.
     *
     * @param x x coordinate
     * @param y y coordinate
     */

    private void updateRobotCoordinates(int x, int y, Player player, int speed) {
        if (speed == 1) {
            switch (orientation) {
                case UP: //NORTH
                    player.getRobot().setPosition(x, y - 1);
                    break;
                case DOWN: //SOUTH
                    player.getRobot().setPosition(x, y + 1);
                    break;
                case LEFT: //EAST
                    player.getRobot().setPosition(x + 1, y);
                    break;
                case RIGHT: //WEST
                    player.getRobot().setPosition(x - 1, y);
                    break;
            }
        } else if (speed == 2) {
            switch (orientation) {
                case UP: //NORTH
                    player.getRobot().setPosition(x, y - 2);
                    break;
                case DOWN: //SOUTH
                    player.getRobot().setPosition(x, y + 2);
                    break;
                case LEFT: //EAST
                    player.getRobot().setPosition(x + 1, y);
                    break;
                case RIGHT: //WEST
                    player.getRobot().setPosition(x - 2, y);
                    break;
            }
        }
    }
}
