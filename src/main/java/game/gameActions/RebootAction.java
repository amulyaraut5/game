package game.gameActions;

import game.Player;
import utilities.Coordinate;
import utilities.JSONProtocol.body.Reboot;
import utilities.enums.Orientation;

import java.util.Random;

/**
 * This class takes care of rebooting the robots.
 *
 * @author annika
 */
public class RebootAction extends Action {

    /**
     * This method is used to carry out the reboot action.
     * Depending on where the robot is located, it is rebooted either on the reboot token or on the starting point.
     * If multiple robots reboot on the same board or if a robot sits on the reboot token when other robots are rebooting,
     * robots will leave the reboot space in the order they rebooted.
     *
     * @param player is the player who is affected by the game action.
     */
    @Override
    public void doAction(Player player) {
        Random random = new Random();

        //Draw two spam cards
        game.getActivationPhase().drawDamage(game.getSpamDeck(), player, 2);

        //discard cards in registers on discard pile
        player.discardCards(player.getRegisterCards(), player.getDiscardedProgrammingDeck());

        //Reboot on rebootTile
        if (player.getRobot().getCoordinate().getX() > 2) {
            player.getRobot().rotateTo(Orientation.UP);
            player.getRobot().moveTo(map.getRestartPoint());
            clearRebootTile(player);
        }
        //Reboot on particular startingTile
        else {
            player.getRobot().setCoordinate(player.getRobot().getStartingPoint());
            Coordinate newPosition = game.getActivationPhase().getActivationElements().calculateNew(player, Orientation.UP);
            if (isTaken(player) && (map.isWallBlocking(newPosition, Orientation.DOWN) || map.isWallBlocking(player.getRobot().getCoordinate(), Orientation.UP))) {
                Coordinate randomStartPoint = map.getStartingPoints().get(random.nextInt(map.getStartingPoints().size()));
                player.getRobot().rotateTo(Orientation.UP);
                player.getRobot().moveTo(randomStartPoint);
            } else {
                player.getRobot().rotateTo(Orientation.UP);
                player.getRobot().moveTo(player.getRobot().getStartingPoint());
            }
            clearStartingPoint(player);
        }
        server.communicateAll(new Reboot(player.getID()));
    }

    /**
     * Checks whether the reboot tile is already taken.
     * @param player the rebooting player
     * @return true if the reboot tile is taken otherwise false
     */
    public boolean isTaken(Player player) {
        for (Player robotOnReboot : game.getPlayers()) {
            if (player.getRobot().getStartingPoint().equals(robotOnReboot.getRobot().getCoordinate()) && robotOnReboot != player) {
                return true;
            }
        }
        return false;
    }

    /**
     * Clears the starting point for the rebooting robot.
     * If the starting point is taken, the robot is moved to the north.
     * @param player the rebooting player
     */
    public void clearStartingPoint(Player player) {
        for (Player robotOnReboot : game.getPlayers()) {
            if (player.getRobot().getStartingPoint().equals(robotOnReboot.getRobot().getCoordinate()) && robotOnReboot != player) {
                robotOnReboot.getRobot().rotateTo(Orientation.UP);
                game.getActivationPhase().handleMove(robotOnReboot, Orientation.UP);
            }
        }
    }

    /**
     * Clears the reboot token for the rebooting robot.
     * If the reboot token is taken, the robot is moved in the direction indicated by the arrow on the reboot token.
     * @param player the rebooting player
     */
    public void clearRebootTile(Player player) {
        for (Player robotOnReboot : game.getPlayers()) {
            if (map.getRestartPoint().equals(robotOnReboot.getRobot().getCoordinate()) && robotOnReboot != player) {
                if (map.getRestartPoint().toPosition() == 0) {
                    robotOnReboot.getRobot().rotateTo(Orientation.RIGHT);
                    game.getActivationPhase().handleMove(robotOnReboot, Orientation.RIGHT);
                } else {
                    robotOnReboot.getRobot().rotateTo(Orientation.DOWN);
                    game.getActivationPhase().handleMove(robotOnReboot, Orientation.DOWN);
                }
            }
        }
    }

}
