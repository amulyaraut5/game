package game.gameObjects.tiles;

import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.enums.AttributeType;

public class StartPoint extends Attribute {

    public StartPoint() {
        type = AttributeType.StartPoint;
    }

    @Override
    public Node createImage() {
        return ImageHandler.createImageView("/tiles/startPoint.png");
    }
}