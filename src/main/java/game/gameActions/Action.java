package game.gameActions;

import game.Player;

public abstract class Action implements Cloneable{

        /**
         * This method is called when a action should be executed.
         * @param player is the player who is affected by the gameaction.
         */
        public abstract void doAction(Player player);

        @Override
        public Object clone() throws CloneNotSupportedException {
                return super.clone();
        }
}
