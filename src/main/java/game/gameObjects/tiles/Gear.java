package game.gameObjects.tiles;

import game.Player;
import game.gameActions.RotateRobot;
import javafx.scene.canvas.GraphicsContext;
import utilities.Coordinate;
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

        new RotateRobot(this.orientation).doAction(this.orientation,player);

    }

    @Override
    public void draw(GraphicsContext gc, Coordinate position) {

    }

}