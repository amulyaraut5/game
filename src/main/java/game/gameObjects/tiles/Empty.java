package game.gameObjects.tiles;

import game.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Empty extends Attribute {

    public Empty() {
        this.type = "Empty";
    }


    @Override
    public void performAction(Player player) {
        // No functionality
    }

    @Override
    public ImageView createImage() {
        var stream = getClass().getResourceAsStream("/tiles/empty.png");
        var image = new Image(stream, 60, 60, true, true);

        return new ImageView(image);
    }
}
