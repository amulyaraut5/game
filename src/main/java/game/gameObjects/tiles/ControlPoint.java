package game.gameObjects.tiles;

import game.Player;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.CheckpointReached;
import utilities.JSONProtocol.body.GameWon;

/**
 * @author Amulya
 */

public class ControlPoint extends Attribute {

    private int count; //number of the ControlPoint

    /**
     * Constructor that helps in creating multiple instances of Checkpoints with different ID
     *
     * @param count
     */

    public ControlPoint(int count) {
        this.count = count;
        this.type = "ControlPoint";
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

        JSONMessage jsonMessage = new JSONMessage(new CheckpointReached(player.getId(),this.count));
        userThread.sendMessage(jsonMessage);

        // TODO Change no. of checkPoints based on Map
        if(game.getNoOfCheckPoints()==1){

            // Send JSONMESSAGE to all users/players

            JSONMessage jsonMessage1 = new JSONMessage(new GameWon(player.getId()));
            client.sendMessage(jsonMessage1);

            // TODO End the game:
        }
        else if (game.getNoOfCheckPoints() != 1) {

            if (player.getCheckPointCounter() > this.count){
                logger.info("Checkpoint already reached");
                // Maybe inform all the clients and users
            }
            else if (player.getCheckPointCounter() < 1){
                logger.info(" 1st Checkpoint not reached.");
            }
            else if (player.getCheckPointCounter() == 1){
                int checkPoint = player.getCheckPointCounter();
                checkPoint++;
                player.setCheckPointCounter(checkPoint);

                if ((game.getNoOfCheckPoints() == 2) && (this.count == 2)) {

                    JSONMessage jsonMessage1 = new JSONMessage(new GameWon(player.getId()));
                    client.sendMessage(jsonMessage1);

                    // TODO End the game
                }
            }

        }

    }
}