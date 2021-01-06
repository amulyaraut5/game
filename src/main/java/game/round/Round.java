package game.round;

import game.Game;
import game.Player;
import server.Server;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.ActivePhase;

import java.util.ArrayList;

/** this class implements a Round.
 * a Round consists of the three phases UpgradePhase, ProgrammingPhase and ActivationPhase
 * in this exact order.
 */

public class Round {


	/**
	 * the game which created the round
	 */
	private Game game;

	/**
	 * the list which contains the players of the round (they get kicked out of this list if they have to reboot)
	 */
	protected ArrayList<Player> playerList;

	public Round(Game game) {
		this.game = game;
		this.playerList = game.getPlayerList();
		executeRound();
	}



	private void discardCards() {

	}



	public void executeRound(){
		//Aufbauphase //TODO where ?
		JSONMessage buildPhaseMessage = new JSONMessage(new ActivePhase(0));
		Server.getInstance().communicateAll(buildPhaseMessage);

		//start upgradePhase and upgradePhase.startUpgradePhase()
		JSONMessage upgradePhaseMessage = new JSONMessage(new ActivePhase(1));
		Server.getInstance().communicateAll(upgradePhaseMessage);

		//start programmingPhase and programmingPhase.startProgrammingPhase()
		JSONMessage programmingPhaseMessage = new JSONMessage(new ActivePhase(2));
		ProgrammingPhase programmingPhase = new ProgrammingPhase(this);
		Server.getInstance().communicateAll(programmingPhaseMessage);


		//start activationPhase
		JSONMessage activationPhaseMessage = new JSONMessage(new ActivePhase(3));
		Server.getInstance().communicateAll(activationPhaseMessage);

	}

	public void resetRound(){

	}

	public ArrayList<Player> getPlayerList() {
		return this.playerList;
	}
}