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
	private ProgrammingPhase programmingPhase;

	public GameTimer( ProgrammingPhase programmingPhase){

		this.programmingPhase = programmingPhase;

	}

	/**
	 * a method that starts a 30 sec timer
	 */
	@Override
	public void run(){
		Server.getInstance().communicateAll(new TimerStarted());
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		programmingPhase.timerHasEnded();
	}


}