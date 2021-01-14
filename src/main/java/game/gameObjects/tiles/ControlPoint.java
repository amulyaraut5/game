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

    /**
     * Checkpoint is the final destination of the game and player wins the game as
     * soon as the player has reached all the checkpoints in chronological order.
     * The player gets the checkpoint token.
     *
     * @param player
     */
    @Override
    public void performAction(Player player) {

        // Send CheckpointReached protocol to all users/players

        player.message(new CheckpointReached(player.getId(), this.count));

        // TODO Change no. of checkPoints based on Map
        if (game.getNoOfCheckPoints() == 1) {

            // Send JSONMESSAGE to all users/players

            //JSONMessage jsonMessage1 = new JSONMessage(new GameWon(player.getId())); //TODO remove, Send from Server to Client
            //client.sendMessage(jsonMessage1);

            // TODO End the game:
        } else if (game.getNoOfCheckPoints() != 1) {

            if (player.getCheckPointCounter() > this.count) {
                logger.info("Checkpoint already reached");
                // Maybe inform all the clients and users

            } else if (player.getCheckPointCounter() < 1) {
                logger.info(" 1st Checkpoint not reached.");

            } else if (player.getCheckPointCounter() == 1) {
                int checkPoint = player.getCheckPointCounter();
                checkPoint++;
                player.setCheckPointCounter(checkPoint);

                if ((game.getNoOfCheckPoints() == 2) && (this.count == 2)) {

                    //JSONMessage jsonMessage1 = new JSONMessage(new GameWon(player.getId())); //TODO remove, Send from Server to Client
                    //client.sendMessage(jsonMessage1);

                    // TODO End the game
                }
            }

        }

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