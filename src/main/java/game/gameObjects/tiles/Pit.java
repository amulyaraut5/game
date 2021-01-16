package game.gameObjects.tiles;

import game.Player;
import game.gameActions.RebootAction;
import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.JSONProtocol.body.Reboot;
import utilities.Orientation;
import utilities.Utilities.AttributeType;

/**
 * @author Amulya
 */
public class Pit extends Attribute {

    public Pit() {
        type = AttributeType.Pit;
    }



    @Override
    public Node createImage() {
        return ImageHandler.createImageView("/tiles/pit.png");
    }
}