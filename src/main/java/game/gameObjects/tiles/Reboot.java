package game.gameObjects.tiles;

import game.Player;
import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.Utilities.AttributeType;

public class Reboot extends Attribute {

    public Reboot() {
        type = AttributeType.Reboot;
    }

    @Override
    public void performAction(Player player) {
    //You can be shot or pushed by an active robot while on a reboot token, but you cannot shoot robots.
    //You also may not use upgrades.
    }

    @Override
    public Node createImage() {
        return ImageHandler.createImageView("/tiles/reboot.png");
    }
}