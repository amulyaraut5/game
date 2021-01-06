package game.gameObjects.tiles;

import game.Player;
import game.gameActions.MoveRobot;
import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.Utilities.Orientation;

/**
 * @author Amulya
 */

public class Belt extends Attribute {

    private int speed; // 1 = Green Conveyor, 2 = Blue Conveyor

    public Belt(Orientation orientation, int speed) {
        this.speed = speed;
        this.orientation = orientation;
        this.type = "Belt";
    }

    /**
     * The Belt pushes the robot in the direction of tile by
     * one space or two space based on speed of Belt.
     * Once a robot has moved off a belt, the belt has no longer effect.
     *
     * @param player
     */
    @Override
    public void performAction(Player player) {

        if (collisionPointExist()) {
            // No movement
        } else {
            if (speed == 1) {
                new MoveRobot().doAction(this.orientation, player);

            } else {
                new MoveRobot().doAction(this.orientation, player);
                new MoveRobot().doAction(this.orientation, player);
            }
        }
    }

    @Override
    public Node createImage() {
        String path = "";
        if (speed == 1) path = "/tiles/green.png";
        else if (speed == 2) path = "/tiles/blue.png";

        return ImageHandler.createImageView(path, orientation);
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
