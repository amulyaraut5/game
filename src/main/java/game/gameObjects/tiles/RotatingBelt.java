package game.gameObjects.tiles;

import game.Player;
import javafx.scene.canvas.GraphicsContext;
import utilities.Coordinate;
import utilities.Utilities.Orientation;

/**
 * @author Amulya
 */

public class RotatingBelt extends Attribute {


    private int speed; // 1 = Blue Conveyor, 2 = Green Conveyor
    private Orientation[] orientations; // [0] = Running direction, [1] = rotation direction
    private boolean isCrossing; // true = crossing, false = curve


    public RotatingBelt(Orientation[] orientations, boolean isCrossing, int speed) {
        this.orientations = orientations;
        this.isCrossing = isCrossing;
        this.speed = speed;
        this.type = "RotatingBelt";

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

        Orientation orientation = player.getRobot().getOrientation();
        // Conveyor does not really change the direction of robot  unless......

        // Then check the whether there is collision point exist or not.
        // Then we update the location of Robot in the direction of Conveyor.

        if (collisionPointExist()) {
            // No movement
        } else {
            // Need of location of robot and direction of tile.
            updateRobotCoordinates(xCoordinate, yCoordinate, player);
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

    private void updateRobotCoordinates(int x, int y, Player player) {
        switch (orientations[0]) {
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
    }
}