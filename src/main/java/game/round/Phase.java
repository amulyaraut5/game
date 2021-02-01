package game.round;

import game.Game;
import game.Player;
import game.gameObjects.maps.Map;
import server.Server;

import java.util.ArrayList;

public abstract class Phase {
    protected Server server = Server.getInstance();
    protected Game game = Game.getInstance();
    protected ArrayList<Player> players = game.getPlayers();
    protected Map map  = game.getMap();

    public Phase() {
    }
}
