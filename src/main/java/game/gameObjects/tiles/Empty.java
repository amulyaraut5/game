package game.gameObjects.tiles;

import game.Player;
import javafx.scene.canvas.GraphicsContext;
import utilities.Coordinate;

public class Empty extends Attribute {

    public Empty() {
        this.type = "Empty";
    }


    @Override
    public void performAction(Player player) {
        // No functionality
    }

    @Override
    public void draw(GraphicsContext gc, Coordinate position) {

    }
}
