package game.gameActions;

import game.Player;
import game.gameObjects.cards.Card;
import utilities.Orientation;

public class RebootAction extends Action{

    /**
     * If the robot falls off the board or into a pit, or if a worm card is activated,
     * the robot must be rebooted.
     * @param orientation
     * @param player is the player who is affected by the game action.
     */
    @Override
    public void doAction(Orientation orientation, Player player) {
        //Draw two spam cards
        for (int i = 0; i < 2; i++) {
            Card spamCard = player.getGame().getSpamDeck().pop();
            player.getDiscardedProgrammingDeck().getDeck().add(spamCard);
        }
        //discard cards in registers on discard pile
        player.getRegister().addAll(player.getDiscardedProgrammingDeck().getDeck());
        player.getRegister().clear();
        //Out of the round, must wait until the next round to program the robot again.
        //game.getActiveRound().getPlayerList().remove(player);
        player.freeze();
        //TODO Robot is placed on reboot token
        //Coordinate rebootTile = reboot.getPosition(); //?
        //player.getRobot().setPosition(rebootTile);
        //TODO The player can turn the robot to face any direction.
    }
}
