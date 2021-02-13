package game.gameObjects.tiles;

import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.enums.AttributeType;
import utilities.enums.Orientation;

/**
 * @author Simon
 */

public class RotatingBelt extends Attribute {

    private final Orientation[] orientations; // [0] = running direction, [1] = rotation direction
    private final boolean isCrossing; // true = crossing, false = curve
    private final int speed; // 1 = Blue Conveyor, 2 = Green Conveyor

    public RotatingBelt(Orientation[] orientations, boolean isCrossing, int speed) {
        this.orientations = orientations;
        this.isCrossing = isCrossing;
        this.speed = speed;
        type = AttributeType.RotatingBelt;
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

        var imageView = ImageHandler.createImageView(path);

        switch (orientations[0]) {
            case UP -> {
                if (orientations[1] == Orientation.RIGHT) imageView.setScaleX(-1f);
            }
            case RIGHT -> {
                if (orientations[1] == Orientation.DOWN) imageView.setScaleX(-1f);
                imageView.setRotate(90);
            }
            case DOWN -> {
                if (orientations[1] == Orientation.LEFT) imageView.setScaleX(-1f);
                imageView.setRotate(180);
            }
            case LEFT -> {
                if (orientations[1] == Orientation.UP) imageView.setScaleX(-1f);
                imageView.setRotate(270);
            }
        }
        return imageView;
    }

    public int getSpeed() {
        return speed;
    }

    public Orientation[] getOrientations() {
        return orientations;
    }

    public boolean isCrossing() {
        return isCrossing;
    }
}