package game.gameObjects.tiles;

import game.Player;
import game.gameActions.RotateRobot;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utilities.Utilities.Orientation;

/**
 * @author Amulya
 */

public class Gear extends Attribute {

    private Orientation orientation; //gear rotation: RIGHT = clockwise, LEFT = Anti-clockwise

    public Gear(Orientation orientation) {
        this.orientation = orientation;
        this.type = "Gear";
    }

    /**
     * Rotates the robot in anti-clockwise direction and clock wise direction.
     * Orientation is initialized by constructor.
     *
     * @param player
     */

    @Override
    public void performAction(Player player) {

        new RotateRobot(this.orientation).doAction(this.orientation, player);

    }

    @Override
    public Node createImage() {
        String path;
        if (orientation == Orientation.RIGHT) {
            path = "/tiles/gear_right.png";
        } else {
            path = "/tiles/gear_left.png";
        }

        var stream = getClass().getResourceAsStream(path);
        var image = new Image(stream, 60, 60, true, true);

        return new ImageView(image);
    }

}