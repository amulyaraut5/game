package game.gameObjects.tiles;

import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.enums.AttributeType;
import utilities.enums.Orientation;

/**
 * @author Simon
 */

public class Antenna extends Attribute {

    private final Orientation orientation;

    /**
     * Constructor for Antenna that basically sets the antenna facing north
     */
    public Antenna() {
        orientation = Orientation.RIGHT;
        type = AttributeType.Antenna;
    }

    @Override
    public Node createImage() {
        return ImageHandler.createImageView("/tiles/antenna.png");
    }

    public Orientation getOrientation() {
        return orientation;
    }
}