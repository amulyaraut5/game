package game.gameObjects.tiles;

import game.Player;
import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.Utilities.AttributeType;
import utilities.Utilities.Rotation;

/**
 * @author Amulya
 */

public class Gear extends Attribute {

    private Rotation orientation; //gear rotation: RIGHT = clockwise, LEFT = Anti-clockwise

    public Gear(Rotation orientation) {
        this.orientation = orientation;
        type = AttributeType.Gear;
    }


    @Override
    public Node createImage() {
        String path;
        if (orientation == Rotation.RIGHT) {
            path = "/tiles/gear_right.png";
        } else {
            path = "/tiles/gear_left.png";
        }

        return ImageHandler.createImageView(path);
    }
    public Rotation getOrientation() {
        return orientation;
    }
}