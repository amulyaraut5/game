package game.gameObjects.tiles;

import game.Player;
import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.Reboot;
import utilities.Utilities.AttributeType;

/**
 * @author Amulya
 */
public class Pit extends Attribute {

    public Pit() {
        this.type = AttributeType.PushPanel;
    }

    /**
     * As soon as the robot finds itself in Pit, the player will be out
     * of the round and gets spam card which gets collected in discard pile.
     * Place the robot on the reboot token that is on the same board where the robot was
     * when it rebooted. Robot may face any direction. If the Robot is  rebooted from
     * the start board, place the  robot on the space from where  the game was started
     *
     * @param player
     */
    @Override
    public void performAction(Player player) {

        // Set the position of robot in the position of reboot token
        // Todo Set the position of reboot token

        player.getRobot().setPosition(4, 5);
        player.freeze();

        // Player needs to draw spam card from the deck and add to the discard pile

        JSONMessage jsonMessage = new JSONMessage(new Reboot(player.getId()));
        userThread.sendMessage(jsonMessage);
    }

    @Override
    public Node createImage() {
        return ImageHandler.createImageView("/tiles/pit.png");
    }
}