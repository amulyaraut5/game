package game.gameObjects.tiles;

import game.Player;
import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.Utilities.AttributeType;

public class RestartPoint extends Attribute {

    public RestartPoint() {
        type = AttributeType.RestartPoint;
    }


    @Override
    public Node createImage() {
        return ImageHandler.createImageView("/tiles/restart.png");
    }
}