package game.round;

import game.Game;
import game.Player;
import server.Server;
import utilities.JSONProtocol.body.ActivePhase;

import java.util.ArrayList;

/**
 * this class implements a Round.
 * a Round consists of the three phases UpgradePhase, ProgrammingPhase and ActivationPhase
 * in this exact order.
 */

public class Round {


    /**
     * the list which contains the players of the round (they get kicked out of this list if they have to reboot)
     */
    protected ArrayList<Player> playerList;
    /**
     * the game which created the round
     */
    private Server server = Server.getInstance();
    private Game game = Game.getInstance();

    public Round(Game game) {
        this.game = game;
        this.playerList = game.getPlayerList();
        executeRound();
    }


    private void discardCards() {

    }


    public void executeRound() {
        //Aufbauphase //TODO where ?
        server.communicateAll(new ActivePhase(0));

        //start upgradePhase and upgradePhase.startUpgradePhase()
        server.communicateAll(new ActivePhase(1));

        //start programmingPhase and programmingPhase.startProgrammingPhase()
        ProgrammingPhase programmingPhase = new ProgrammingPhase();
        server.communicateAll(new ActivePhase(2));


        //start activationPhase
        server.communicateAll(new ActivePhase(3));

    }

    public void resetRound() {

    }

    public ArrayList<Player> getPlayerList() {
        return this.playerList;
    }
}