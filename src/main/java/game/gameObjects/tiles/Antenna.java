package game.gameObjects.tiles;

import game.Player;
import javafx.scene.canvas.GraphicsContext;
import utilities.Coordinate;
import utilities.Utilities.Orientation;

/**
 * @author Amulya
 */

public class Antenna extends Attribute {

    private Orientation orientation;

    /**
     * Constructor for Antenna that basically sets the antenna facing
     * north in the specific tile.
     */
    public Antenna() {
        this.orientation = Orientation.UP;// means here north
        this.type = "Antenna";
    }

    /**
     * Antenna does not really have a function besides it act as
     * shield and cannot be moved by any robot.
     *
     * @param player
     */
    @Override
    public void performAction(Player player) {
        // Does Nothing
    }

    @Override
    public void draw(GraphicsContext gc, Coordinate position) {

    }

}