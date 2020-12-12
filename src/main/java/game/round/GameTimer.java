package game.round;

import game.Player;

/**
 * if one player is ready with picking cards, he creates an instance of timer (if timer isn´t running yet)
 */

public class GameTimer extends ProgrammingPhase{

	public GameTimer(Player callingPlayer){
		super();
		//start 30 sec timer
		startTimer();
		//after 30 sec call for every player in notReadyPlayers of ProgrammingPhase the method timerunout
		// for (Player player : notReadyPlayers){
		//	if (player.name != callingPlayer.name){
		//		player.timeRunOut();}}

	}

	/**
	 * a method that starts a 30 sec timer
	 */
	public void startTimer(){

	}

}