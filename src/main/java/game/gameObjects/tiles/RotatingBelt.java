package game.gameObjects.tiles;

import game.Player;
import game.gameActions.MoveRobotInCurve;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utilities.Utilities.Orientation;

/**
 * @author Amulya
 */

public class RotatingBelt extends Attribute {


    private Orientation[] orientations; // [0] = running direction, [1] = rotation direction
    private boolean isCrossing; // true = crossing, false = curve
    private int speed; // 1 = Blue Conveyor, 2 = Green Conveyor

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
    public Node createImage() {
        String color = null;
        if (speed == 1) color = "green";
        else if (speed == 2) color = "blue";

        String rotation;
        if (isCrossing) rotation = "crossing";
        else rotation = "curve";

        String path = "/tiles/" + color + "_" + rotation + ".png";

        var stream = getClass().getResourceAsStream(path);
        var image = new Image(stream, 60, 60, true, true);
        var imageView = new ImageView(image);

        switch (orientations[0]) {
            case UP -> {
                if (orientations[1] == Orientation.RIGHT) {
                    imageView.setScaleX(-1f);
                }
            }
            case RIGHT -> {
                if (orientations[1] == Orientation.DOWN) {
                    imageView.setScaleX(-1f);
                }
                imageView.setRotate(90);
            }
            case DOWN -> {
                if (orientations[1] == Orientation.LEFT) {
                    imageView.setScaleX(-1f);
                }
                imageView.setRotate(180);
            }
            case LEFT -> {
                if (orientations[1] == Orientation.UP) {
                    imageView.setScaleX(-1f);
                }
                imageView.setRotate(270);
            }
        }
        return imageView;
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