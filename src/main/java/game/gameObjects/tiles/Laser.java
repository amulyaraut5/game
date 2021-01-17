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