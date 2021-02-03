package game.gameActions;

import game.Game;
import game.Player;
import game.gameObjects.maps.Map;
import server.Server;
import utilities.enums.Orientation;

/**
 * The abstract class Action is then extended by different sub-classes
 * based on different type of action to be performed.
 */
public abstract class Action {
    protected final Server server = Server.getInstance();
    protected final Game game = Game.getInstance();
    protected final Map map = game.getMap();

    /**
     * This method is called when a action should be executed.
     *
     * @param player is the player who is affected by the game action.
     */
    public abstract void doAction(Player player);
}
