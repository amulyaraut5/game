package game.gameObjects.tiles;

import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.enums.AttributeType;
import utilities.enums.Orientation;

/**
 * @author Simon
 */

public class Laser extends Attribute {

    private final Orientation orientation;
    private final int count; // number of lasers

    public Laser(Orientation orientation, int count) {
        this.orientation = orientation;
        this.count = count;
        type = AttributeType.Laser;
    }

    @Override
    public Node createImage() {
        String path = "/tiles/laser/laser_" + count + "c.png";
        return ImageHandler.createImageView(path, orientation);
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public int getCount() {
        return count;
    }
}