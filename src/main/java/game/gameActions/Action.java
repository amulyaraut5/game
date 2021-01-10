package game.gameActions;

import game.Player;
import utilities.Orientation;

/**
 * The abstract class Action is then extended by different sub-classes
 * based on different type of action to be performed.
 */
public abstract class Action {

   /**
    * This method is called when a action should be executed.
    * @param player is the player who is affected by the gameaction.
    */
    public abstract void doAction(Orientation orientation, Player player);
}
