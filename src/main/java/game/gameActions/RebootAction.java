package game.gameActions;

import game.Player;
import utilities.Coordinate;
import utilities.JSONProtocol.body.Reboot;
import utilities.enums.Orientation;

import java.util.Random;


/**
 * @author annika
 */
public class RebootAction extends Action {

    /**
     * If the robot falls off the board or into a pit, or if a worm card is activated,
     * the robot must be rebooted.
     * Note: If multiple robots reboot on the same board in the same round or if a robot sits on the reboot token when other robots are rebooting,
     * robots will leave the reboot space in the order they rebooted, with the next robot pushing the robot before it in the direction indicated by the arrow on the reboot token.
     *
     * @param player is the player who is affected by the game action.
     */
    @Override
    public void doAction(Player player) {
        Random random = new Random();

        //rebootedPlayers.add(player);
        //Draw two spam cards
        game.getActivationPhase().drawDamage(game.getSpamDeck(), player, 2);

        //discard cards in registers on discard pile and create new empty register
        player.discardCards(player.getRegisterCards(), player.getDiscardedProgrammingDeck());

        //Robot is placed on reboot token
        if (player.getRobot().getCoordinate().getX() > 2) {
            player.getRobot().rotateTo(Orientation.UP);
            player.getRobot().moveTo(map.getRestartPoint());
        } else {
            player.getRobot().setCoordinate(player.getRobot().getStartingPoint());
            Coordinate newPosition = game.getActivationPhase().getActivationElements().calculateNew(player, Orientation.UP);
            if(isTaken(player) && (map.isWallBlocking(newPosition, Orientation.DOWN) || map.isWallBlocking(player.getRobot().getCoordinate(), Orientation.UP))){
                Coordinate randomStartPoint = map.getStartingPoints().get(random.nextInt(map.getStartingPoints().size()));
                player.getRobot().rotateTo(Orientation.UP);
                player.getRobot().moveTo(randomStartPoint);
            } else {
                player.getRobot().rotateTo(Orientation.UP);
                player.getRobot().moveTo(player.getRobot().getStartingPoint());
            }
        }
        clearRebootTile(player);
        server.communicateAll(new Reboot(player.getID()));

        /*activePlayers.remove(player);

        if (activePlayers.isEmpty()) {
            activePlayers.addAll(rebootedPlayers);
            rebootedPlayers.clear();
            game.nextPhase();
        }*/
    }

    public boolean isTaken(Player player){
        for (Player robotOnReboot : game.getPlayers()) {
            if (player.getRobot().getStartingPoint().equals(robotOnReboot.getRobot().getCoordinate()) && robotOnReboot != player) {
                return true;
            }
        }
        return false;
    }


    public void clearRebootTile(Player player){
        for (Player robotOnReboot : game.getPlayers()) {
            if (player.getRobot().getStartingPoint().equals(robotOnReboot.getRobot().getCoordinate()) && robotOnReboot != player) {
                robotOnReboot.getRobot().rotateTo(Orientation.UP);
                game.getActivationPhase().handleMove(robotOnReboot, Orientation.UP);
            }
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
