package game.gameObjects.tiles;

import game.Player;
import game.gameActions.MoveRobotInCurve;
import javafx.scene.image.ImageView;
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
     * The RotatingBelt moves a Robot in a non-linear fashion.
     *
     * @param player
     */
    @Override
    public void performAction(Player player) {
        if (collisionPointExist()) {
            // No movement
        } else {
            if (speed == 2) {
                new MoveRobotInCurve().doAction2(this.orientations, player);
            }
        }
    }

    @Override
    public ImageView createImage() {
        return new ImageView();
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