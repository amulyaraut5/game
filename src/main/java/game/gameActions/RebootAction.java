package game.gameActions;

import game.Player;
import utilities.JSONProtocol.body.Movement;
import utilities.JSONProtocol.body.Reboot;
import utilities.enums.Orientation;

/**
 * @author annika
 */
public class RebootAction extends Action {

    /**
     * If the robot falls off the board or into a pit, or if a worm card is activated,
     * the robot must be rebooted.
     * TODO: Note: If multiple robots reboot on the same board in the same round or if a robot sits on the reboot token when other robots are rebooting,
     * robots will leave the reboot space in the order they rebooted, with the next robot pushing the robot before it in the direction indicated by the arrow on the reboot token.
     *
     * @param orientation
     * @param player      is the player who is affected by the game action.
     */
    @Override
    public void doAction(Orientation orientation, Player player) {
        //Draw two spam cards
        //TODO handle empty Spam-Deck -> in handleEmptyDeck() ?
        game.getSpamDeck().drawTwoSpam(player);

        //discard cards in registers on discard pile and create new empty register
        player.discardCards(player.getRegisterCards(), player.getDiscardedProgrammingDeck());

        //Robot is placed on reboot token
        player.getRobot().setCoordinate(map.getRestartPoint());
        int restartPos = map.getRestartPoint().toPosition();

        //Out of the round, must wait until the next round to program the robot again.
        game.getActivationPhase().getRebootedPlayers().add(player);
        game.getActivationPhase().getActivePlayers().remove(player);

        server.communicateAll(new Movement(player.getID(), restartPos));
        server.communicateAll(new Reboot(player.getID()));

        //TODO The player can turn the robot to face any direction.
    }
}
