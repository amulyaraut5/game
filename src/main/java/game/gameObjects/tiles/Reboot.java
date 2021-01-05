package game.gameObjects.tiles;

import game.Player;
import javafx.scene.Node;
import utilities.ImageHandler;

public class Reboot extends Attribute {

    public Reboot() {
        type = "Reboot";
    }

    @Override
    public void performAction(Player player) {

    }

    @Override
    public Node createImage() {
        return ImageHandler.createImageView("/tiles/reboot.png");
    }
}