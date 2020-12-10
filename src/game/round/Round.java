package game.round;

import game.Game;
import game.Player;

import java.util.ArrayList;

public class Round {


	/**
	 * the game which created the round
	 */
	private Game game;

	/**
	 * the list which contains the players of the round
	 * (they get kicked out of this list if they have to reboot)
	 */
	private ArrayList<Player> activePlayerList = new ArrayList<>();

	public Round() {
	}

	/**
	 * player get their  cards for programming their robot in this round
	 */
	private void dealCards() {
			// for (Player player : activePlayerList){
			//  deal 9 cards
	}

	private void discardCards() {

	}

	public Round(Game game) {
		this.game = game;
		//this.activePlayerList = game.getPlayerList();
		executeRound();
	}

	public void executeRound(){
		// dealCards for every active Player
		//start upgradePhase and upgradePhase.startUpgradePhase()

		//start programmingPhase and programmingPhase.startProgrammingPhase()

		//start programmingPhase and programmingPhase.startProgrammingPhase()

	}

	public void resetRound(){

	}

}