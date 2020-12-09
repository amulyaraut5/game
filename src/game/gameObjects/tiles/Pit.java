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

    @Override
    public void performAction(Player player) {
        // player.getRobot().reboot();
    }
}