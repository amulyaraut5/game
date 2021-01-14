package game.gameObjects.tiles;

import game.Player;
import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.Utilities.AttributeType;

public class Empty extends Attribute {

    public Empty() {
        type = AttributeType.Empty;
    }


    @Override
    public void performAction(Player player) {
        // No functionality
    }

    @Override
    public Node createImage() {
        return ImageHandler.createImageView("/tiles/empty.png");
    }
}
