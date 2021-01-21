package game.gameObjects.tiles;

import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.enums.AttributeType;
import utilities.enums.Rotation;

/**
 * @author Amulya
 */

public class Gear extends Attribute {

    private Rotation rotation; //gear rotation: RIGHT = clockwise, LEFT = Anti-clockwise

    public Gear(Rotation rotation) {
        this.rotation = rotation;
        type = AttributeType.Gear;
    }


    @Override
    public Node createImage() {
        String path;
        if (rotation == Rotation.RIGHT) {
            path = "/tiles/gear_right.png";
        } else {
            path = "/tiles/gear_left.png";
        }

        return ImageHandler.createImageView(path);
    }
    public Rotation getOrientation() {
        return rotation;
    }
}