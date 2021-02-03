package game.gameObjects.tiles;

import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.enums.AttributeType;
import utilities.enums.Orientation;

public class RestartPoint extends Attribute {

    private final Orientation orientation;

    public RestartPoint(Orientation orientation) {
        this.orientation = orientation;
        type = AttributeType.RestartPoint;
    }


    @Override
    public Node createImage() {
        return ImageHandler.createImageView("/tiles/restart.png", orientation);
    }

    public Orientation getOrientation() {
        return orientation;
    }
}