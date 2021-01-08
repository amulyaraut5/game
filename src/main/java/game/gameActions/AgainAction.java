package game.gameActions;

import game.Player;
import utilities.Orientation;

/**
 * Repeats the programming in the players previous register.
 * @author annika
 */
public class AgainAction extends Action{

    @Override
    public void doAction(Orientation orientation, Player player) {
        //TODO if previous register was a damage card, draw card from deck, play that card this register.
            player.setCurrentAction(player.getLastAction());
    }
}
