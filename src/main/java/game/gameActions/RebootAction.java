package game.gameActions;

import game.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.body.Movement;
import utilities.JSONProtocol.body.Reboot;
import utilities.enums.Orientation;

import java.util.ArrayList;

/**
 * @author annika
 */
public class RebootAction extends Action {
    private static final Logger logger = LogManager.getLogger();

    /**
     * If the robot falls off the board or into a pit, or if a worm card is activated,
     * the robot must be rebooted.
     * Note: If multiple robots reboot on the same board in the same round or if a robot sits on the reboot token when other robots are rebooting,
     * robots will leave the reboot space in the order they rebooted, with the next robot pushing the robot before it in the direction indicated by the arrow on the reboot token.
     *
     * @param orientation
     * @param player      is the player who is affected by the game action.
     */
    @Override
    public void doAction(Orientation orientation, Player player) {
        ArrayList<Player> activePlayers = game.getActivationPhase().getActivePlayers();
        ArrayList<Player> rebootedPlayers = game.getActivationPhase().getRebootedPlayers();

        ArrayList<Player> allPlayers = new ArrayList<>();
        allPlayers.addAll(activePlayers);
        allPlayers.addAll(rebootedPlayers);

        for (Player robotOnReboot : allPlayers) {
            if (map.getRestartPoint().equals(robotOnReboot.getRobot().getCoordinate()) && robotOnReboot != player) {
                game.getActivationPhase().handleMove(robotOnReboot, Orientation.DOWN);
            }
        }
        //Draw two spam cards
        game.getActivationPhase().drawDamage(game.getSpamDeck(), player, 2);

        //discard cards in registers on discard pile and create new empty register
        player.discardCards(player.getRegisterCards(), player.getDiscardedProgrammingDeck());

        //Robot is placed on reboot token
        player.getRobot().setOrientation(Orientation.UP); //TODO robots must face north
        player.getRobot().setCoordinate(map.getRestartPoint());
        int restartPos = map.getRestartPoint().toPosition();

        //Out of the round, must wait until the next round to program the robot again.
        rebootedPlayers.add(player);
        activePlayers.remove(player);

        server.communicateAll(new Movement(player.getID(), restartPos));
        server.communicateAll(new Reboot(player.getID()));

        if (activePlayers.isEmpty()) {
            activePlayers.addAll(rebootedPlayers);
            rebootedPlayers.clear();
            game.nextPhase();
        }
    }
}