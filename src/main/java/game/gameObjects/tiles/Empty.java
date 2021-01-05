package game.gameObjects.tiles;

import game.Player;
import javafx.scene.Node;
import utilities.ImageHandler;

public class Empty extends Attribute {

    public Empty() {
        this.type = "Empty";
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
