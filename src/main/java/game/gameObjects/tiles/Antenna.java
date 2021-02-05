package game.gameObjects.tiles;

import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.enums.AttributeType;

/**
 * @author Simon
 */

public class Antenna extends Attribute {

    /**
     * Constructor for Antenna that basically sets the antenna facing north
     */
    public Antenna() {
        type = AttributeType.Antenna;
    }

    @Override
    public Node createImage() {
        return ImageHandler.createImageView("/tiles/antenna.png");
    }
}