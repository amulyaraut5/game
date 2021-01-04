package game.gameObjects.tiles;

import game.Player;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Reboot extends Attribute {

    public Reboot() {
        type = "RebootToken";
    }

    @Override
    public void performAction(Player player) {

    }

    @Override
    public Node createImage() {
        var stream = getClass().getResourceAsStream("/tiles/reboot.png");
        var image = new Image(stream, 60, 60, true, true);

        return new ImageView(image);
    }
}