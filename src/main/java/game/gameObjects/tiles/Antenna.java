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
        this.orientation = Orientation.RIGHT;
        this.type = AttributeType.Antenna;
    }

    /**
     * Antenna does not really have a function besides it act as
     * shield and cannot be moved by any robot.
     *
     * @param player
     */
    @Override
    public void performAction(Player player) {
        // Does Nothing
    }

    @Override
    public Node createImage() {
        return ImageHandler.createImageView("/tiles/antenna.png");
    }


}