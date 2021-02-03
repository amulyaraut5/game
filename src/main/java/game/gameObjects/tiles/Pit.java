package game.gameObjects.tiles;

import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.enums.AttributeType;

/**
 * @author Simon
 */
public class Pit extends Attribute {

    public Pit() {
        type = AttributeType.Pit;
    }



    @Override
    public Node createImage() {
        return ImageHandler.createImageView("/tiles/pit.png");
    }
}