package game.gameObjects.tiles;

import game.Player;
import javafx.scene.image.ImageView;
import utilities.Utilities.Orientation;

/**
 * @author Amulya
 */

public class Antenna extends Attribute {

    private Orientation orientation;

    /**
     * Constructor for Antenna that basically sets the antenna facing north
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
    public ImageView createImage() {
        return new ImageView();
    }

}