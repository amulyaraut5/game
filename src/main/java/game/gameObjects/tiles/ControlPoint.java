package game.gameObjects.tiles;

import game.Player;
import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.CheckpointReached;
import utilities.JSONProtocol.body.GameWon;
import utilities.Utilities.AttributeType;

/**
 * @author Amulya
 */

public class ControlPoint extends Attribute {

    private int count; //number of the ControlPoint

    public ControlPoint(int count) {
        this.count = count;
        type = AttributeType.ControlPoint;
    }

    public int getCheckPointID() {
        return count;
    }


    @Override
    public Node createImage() {
        String path = "/tiles/controlPoint/controlPoint_" + count + ".png";
        return ImageHandler.createImageView(path);
    }

    public int getCount() {
        return count;
    }
}