package game.gameObjects.tiles;

import game.Player;
import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.Utilities.AttributeType;

public class StartPoint extends Attribute {

    public StartPoint() {
        type = AttributeType.StartPoint;
    }

    @Override
    public void performAction(Player player) {
    }

    @Override
    public Node createImage() {
        return ImageHandler.createImageView("/tiles/startPoint.png");
    }
}