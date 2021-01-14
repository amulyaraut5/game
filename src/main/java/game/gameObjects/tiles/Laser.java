package game.gameObjects.tiles;

import game.Player;
import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.Orientation;
import utilities.Utilities.AttributeType;


/**
 * @author Amulya
 */

public class Laser extends Attribute {

    private Orientation orientation;
    private int count; // number of lasers

    public Laser(Orientation orientation, int count) {
        this.orientation = orientation;
        this.count = count;
        this.type = AttributeType.Laser;
    }

    /**
     * The effect of Laser is handled separately in Laser class,
     * as the laser should not only fire when the robot steps on laser tile
     * but also at every instance after every register.
     * The robot can get hit at any tile as long as it is in the range of laser.
     *
     * @param player
     */
    @Override
    public void performAction(Player player) {
        // Read javadoc
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