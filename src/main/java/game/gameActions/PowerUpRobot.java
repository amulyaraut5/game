package game.gameActions;

import game.Player;
import utilities.enums.Orientation;

/**
 * The player gets one energy cube.
 * @author annika
 */
public class PowerUpRobot extends Action{
    @Override
    public void doAction(Orientation orientation, Player player) {
    player.setEnergyCubes(player.getEnergyCubes() + 1);
    }
}
