package game.round;

import game.Game;
import game.Player;
import server.Server;
import utilities.JSONProtocol.body.ActivePhase;
import utilities.Utilities;

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
    private ProgrammingPhase programmingPhase;
    private ActivationPhase activationPhase;
    private UpgradePhase upgradePhase;

    /**
     * the game which created the round
     */
    private Server server = Server.getInstance();
    private Game game = Game.getInstance();

    public Round(Game game) {
        this.game = game;
        this.playerList = game.getPlayerList();
    }


    private void discardCards() {

    }

    /**
     * This method gets called from the phases, it calls the next phase
     * @param phase
     */
    public void nextPhase(Utilities.Phase phase) {
        int phaseNumber = 0; //TODO Aufbauphase im game
        switch(phase){
            case ACTIVATION:
                phaseNumber = 1;
                server.communicateAll(new ActivePhase(phase));
                this.upgradePhase = new UpgradePhase();
                break;
            case UPGRADE:
                phaseNumber = 2;
                server.communicateAll(new ActivePhase(phase));
                this.programmingPhase = new ProgrammingPhase();
                break;
            case PROGRAMMING:
                phaseNumber = 3;
                server.communicateAll(new ActivePhase(phase));
                this.activationPhase = new ActivationPhase();
                break;
            default:
                //
        }
    }

    public void resetRound() {

    }

    public ArrayList<Player> getPlayerList() {
        return this.playerList;
    }
}