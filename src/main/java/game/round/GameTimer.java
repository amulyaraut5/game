package game.round;

import game.Game;
import game.Player;
import server.Server;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.TimerStarted;

/**
 * if one player is ready with picking cards, he creates an instance of timer (if timer isn´t running yet)
 */

public class GameTimer extends Thread {
	private Player callingPlayer;
	private ProgrammingPhase programmingPhase;
	public GameTimer(Player callingPlayer, ProgrammingPhase programmingPhase){
		//start 30 sec timer
		this.callingPlayer = callingPlayer;
		this.programmingPhase = programmingPhase;
		//after 30 sec call for every player in notReadyPlayers of ProgrammingPhase the method timerunout
		// for (Player player : notReadyPlayers){
		//	if (player.name != callingPlayer.name){
		//		player.timeRunOut();}}


	}

	/**
	 * a method that starts a 30 sec timer
	 */
	@Override
	public void run(){
		JSONMessage timerStarted = new JSONMessage(new TimerStarted());
		Server.getInstance().communicateAll(timerStarted);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		programmingPhase.setNotFinished(true);
	}

}