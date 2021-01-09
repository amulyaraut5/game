package game;

import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.SelectCard;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * TODO
 */

public class GameController  {

	private static GameController instance;
	private BlockingQueue<Player> playerQueue = new LinkedBlockingQueue<>();
	private BlockingQueue<SelectCard> selectCardQueue = new LinkedBlockingQueue<>();


	public  static GameController getInstance() {
		if (instance == null) {
			instance = new GameController();
		}
		return instance;
	}
	private JSONMessage jsonMessage;
	public synchronized void setJsonMessage(JSONMessage jsonMessage){
		this.jsonMessage = jsonMessage;
	}

	public void sendToProgrammingPhase(Player player,SelectCard selectCard) throws InterruptedException {
		playerQueue.put(player);
		selectCardQueue.put(selectCard);
	}

}