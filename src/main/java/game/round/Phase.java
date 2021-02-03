package game.round;

import game.Game;
import game.Player;
import game.gameObjects.maps.Map;
import server.Server;

import java.util.ArrayList;

public abstract class Phase {
    protected final Server server = Server.getInstance();
    protected final Game game = Game.getInstance();
    protected final ArrayList<Player> players = game.getPlayers();
    protected final Map map = game.getMap();
}
