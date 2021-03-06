package game.gameObjects.tiles;

import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.enums.AttributeType;
import utilities.enums.Orientation;

/**
 * @author Simon
 */

public class Belt extends Attribute {

    private final int speed; // 1 = Green Conveyor, 2 = Blue Conveyor
    private final Orientation orientation;

    public Belt(Orientation orientation, int speed) {
        this.speed = speed;
        this.orientation = orientation;
        type = AttributeType.Belt;
    }

    @Override
    public Node createImage() {
        String path = "";
        if (speed == 1) path = "/tiles/green.png";
        else if (speed == 2) path = "/tiles/blue.png";

        return ImageHandler.createImageView(path, orientation);
    }

    public int getSpeed() {
        return speed;
    }

    public Orientation getOrientation() {
        return orientation;
    }
}
