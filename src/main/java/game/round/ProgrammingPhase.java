package game.round;

import game.Game;
import game.Player;
import game.gameObjects.cards.Card;
import game.gameObjects.cards.ProgrammingCard;

import java.util.ArrayList;
import java.util.Map;

public class ProgrammingPhase extends Round {
	/**
	 * timerIsRunning will get true if a player creates an instance of timer
	 */
	private boolean timerIsRunning = false;

	/**
	 * a player gets removed if he has already chose 5 cards in the time
	 */
	public ArrayList<Player> notReadyPlayers = new ArrayList<>();

	public ProgrammingPhase(Game game) {
		super(game);
	}

	public ProgrammingPhase() {
		super();
	}


	public void startProgrammingPhase(){
		//start programming phase

	}
	/**
	 * every player can look at their programming cards
	 */
	private void showCards() {

	}

	/**
	 * a player can choose 5 cards and then a timer starts
	 */
	private void setRegisterCards(Player player, Map<Integer, Card> mapCards){
		player.setRegisterAndCards(mapCards);
		if(!timerIsRunning){
			timerIsRunning = true;
			GameTimer timer = new GameTimer(player);
		}

	}

	/**
	 * a method which handles the players who didn´t choose cards in time
	 */
	private void timeRunOut() {
		// random set the programming cards of players´ deck to the registerAndCard Map
	}

	/**
	 * this method gets called after every Round to reset the attributes
	 */
	private void resetProgrammingPhase(){
		timerIsRunning = false;
		notReadyPlayers = null;

	}

}