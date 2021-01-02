package game.gameObjects.tiles;

import game.Player;
import javafx.scene.canvas.GraphicsContext;
import utilities.Coordinate;
import utilities.Utilities.Orientation;

/**
 * @author Amulya
 */

public class Wall extends Attribute {

    private Orientation orientation;
    private Orientation[] orientations;

    /**
     * Constructor for wall for those tiles having only one wall facing in one direction.
     *
     * @param orientation
     */
    public Wall(Orientation orientation) {
        this.orientation = orientation;
        this.type = "Wall";
    }

    /**
     * Constructor for Wall for those tiles which have  two walls in two different direction.
     *
     * @param orientations saves the x and y orientations of the wall
     */
    public Wall(Orientation[] orientations) {
        this.orientations = orientations;
        this.type = "Wall";
    }

    /**
     * Wall itself does not have any functionality but robots cannot
     * move through walls and board lasers cannot pass through walls.
     *
     * @param player
     */
    @Override
    public void performAction(Player player) {
        // Does nothing
    }

    @Override
    public void draw(GraphicsContext gc, Coordinate position) {

    }
}

