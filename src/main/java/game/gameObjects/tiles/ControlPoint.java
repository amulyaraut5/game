package game.gameObjects.tiles;

import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.enums.AttributeType;

/**
 * @author Amulya
 */

public class ControlPoint extends Attribute {

    private int count; //number of the ControlPoint

    public ControlPoint(int count) {
        this.count = count;
        type = AttributeType.ControlPoint;
    }

    public int getCheckPointID() {
        return count;
    }


    @Override
    public Node createImage() {
        String path = "/tiles/controlPoint/controlPoint_" + count + ".png";
        return ImageHandler.createImageView(path);
    }

    public int getCount() {
        return count;
    }
}