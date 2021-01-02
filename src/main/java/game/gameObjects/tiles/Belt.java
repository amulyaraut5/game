package game.gameObjects.tiles;

import game.Player;
import game.gameActions.MoveRobotBy1;
import game.gameActions.MoveRobotBy2;
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

        if (collisionPointExist()) {
            // No movement
        } else {
           if (speed == 1){
               new MoveRobotBy1().doAction(this.orientation,player);
           }
           else{
               new MoveRobotBy2().doAction(this.orientation, player);
           }
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

}
