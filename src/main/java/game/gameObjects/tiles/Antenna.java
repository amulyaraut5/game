package game.gameObjects.tiles;

import game.Player;
import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.Orientation;
import utilities.Utilities.AttributeType;

/**
 * @author Amulya
 */

public class Antenna extends Attribute {

    private Orientation orientation;

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