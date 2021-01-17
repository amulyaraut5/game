package game.gameObjects.tiles;

import game.Player;
import game.gameActions.MoveRobot;
import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.Orientation;
import utilities.Utilities.AttributeType;

/**
 * @author Amulya
 */

public class Belt extends Attribute {

    private int speed; // 1 = Green Conveyor, 2 = Blue Conveyor
    private Orientation orientation;

    public Belt(Orientation orientation, int speed) {
        this.speed = speed;
        this.orientation = orientation;
        type = AttributeType.Belt;
    }


    @Override
    public Node createImage() {
        String path = "";
        if (speed == 1) path = "/tiles/green.png";
        else if (speed == 2) path = "/tiles/blue.png";

        return ImageHandler.createImageView(path, orientation);
    }

    public int getSpeed() {
        return speed;
    }

    public Orientation getOrientation() {
        return orientation;
    }
}
