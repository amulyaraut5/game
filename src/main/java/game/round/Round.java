package game.round;

import game.Game;
import game.Player;

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

		//start upgradePhase and upgradePhase.startUpgradePhase()
		ProgrammingPhase programmingPhase = new ProgrammingPhase(this);

		//start programmingPhase and programmingPhase.startProgrammingPhase()

		//start programmingPhase and programmingPhase.startProgrammingPhase()

	}

	public void resetRound(){

	}

	public ArrayList<Player> getPlayerList() {
		return this.playerList;
	}
}