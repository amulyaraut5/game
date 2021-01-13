package game.round;

import game.Game;
import game.Player;
import server.Server;

import java.util.ArrayList;

public abstract class Phase {
    protected Server server = Server.getInstance();
    protected Game game = Game.getInstance();
    protected Round round = game.getActiveRound();

    protected ArrayList<Player> playerList = game.getPlayerList();

    public Phase() {
    }

    public abstract void startPhase();
}
