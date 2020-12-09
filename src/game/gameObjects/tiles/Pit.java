package game.gameObjects.tiles;

import game.Player;

/**
 *
 * @author Amulya
 */
public class Pit extends Attribute {
    Pit(){
        this.name = "Pit";
    }

    /**
     * As soon as the robot finds itself in Pit, the player will be out
     * of the round and gets spam card which gets collected in discard pile.
     *
     * @param player
     */
    @Override
    public void performAction(Player player) {
        // player.getRobot().reboot();
    }
}