package game.gameActions;

import game.Player;
import utilities.Utilities;

public abstract class Action {

        /**
         * * This method is called when a action should be executed.
         * @param player is the player who is affected by the gameaction.
         */
        public abstract void doAction(Utilities.Orientation orientation, Player player);


}
