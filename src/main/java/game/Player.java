package game;

import game.gameObjects.cards.*;
import game.gameObjects.decks.ProgrammingDeck;
import game.gameObjects.robot.Robot;
import server.User;
import utilities.Utilities.Orientation;
import utilities.Utilities.CardName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class extends user to specify different Attributes for a player.
 */

public class Player extends User {


	/**
	 * a Map which connects the register and the related card the user chooses
	 */
	private Map<Integer, Card> registerAndCards = new HashMap<>();

	private Robot robot;
	private int currentRegister;

	private Card currentAction;
	private Card lastAction;

	private int lastCheckpoint;
	private int energyCubes;

	private ProgrammingDeck drawProgrammingDeck;
	private ProgrammingDeck discardedProgrammingDeck;



	private ArrayList<ProgrammingCard> drawnProgrammingCards;
	private ArrayList<ProgrammingCard> chosenProgrammingCards;

	private ArrayList<PermUpgradeCard> installedUpgrades;
	private ArrayList<TempUpgradeCard> tempUpgradeCards;

	private int checkPointCounter;

	private Orientation direction;

	public Player(Robot robot) {
		this.robot = robot;
		this.direction = Orientation.RIGHT;
		this.energyCubes = 5;
		this.drawProgrammingDeck = getDrawProgrammingDeck();
	}

	public Card getCurrentAction() {
		return currentAction;
	}

	public void setCurrentAction(Card currentAction) {
		this.currentAction = currentAction;
	}

	public Card getLastAction() {
		return lastAction;
	}

	public void setLastAction(Card lastAction) {
		this.lastAction = lastAction;
	}

	public Orientation getDirection() {
		return direction;
	}

	public void setDirection(Orientation direction) {
		this.direction = direction;
	}

	/**
	 * if a player chose a card in a register it gets saved in a map within the player
	 * last time edited: sarah
	 * @param register
	 * @param card
	 */
	public void setRegisterAndCards(int register, String card){
		Card choosedCard = null;
		switch(card){
			case "again":
				choosedCard = new AgainCard();
				break;

			case "moveI":
				choosedCard = new Move1Card();
				break;
			case "moveII":
				choosedCard = new Move2Card();
				break;
			case "moveIII":
				choosedCard = new Move3Card();
				break;

			//TODO other types of cards
			case "null": break;
		}
		registerAndCards.put(register, choosedCard);
	}
	public Map<Integer, Card> getRegisterAndCards() {
		return registerAndCards;
	}

	public Robot getRobot(){
		return this.robot;
	}

	public int getCurrentRegister(){ return currentRegister; }

	public int getCheckPointCounter() {
		return checkPointCounter;
	}

	public void setCheckPointCounter(int checkPointCounter) {
		this.checkPointCounter = checkPointCounter;
	}

	public int getEnergyCubes() {
		return energyCubes;
	}

	public void setEnergyCubes(int energyCubes) {
		this.energyCubes = energyCubes;
	}

	public void setDrawnProgrammingCards(ArrayList<ProgrammingCard> drawnProgrammingCards) {this.drawnProgrammingCards = drawnProgrammingCards;}

	public void setDrawProgrammingDeck(ProgrammingDeck drawProgrammingDeck) {this.drawProgrammingDeck = drawProgrammingDeck;}

	public ProgrammingDeck getDrawProgrammingDeck() {return drawProgrammingDeck;}

	public ProgrammingDeck getDiscardedProgrammingDeck() {return discardedProgrammingDeck;}


	/**
	 * It freezes the player from the current round.
	 * Effect of Pit or falling off from  the map
	 */
    public void freeze() {
    }

	/**
	 * used in ProgrammingPhase. If the DrawProgrammingDeck is empty the discarded pile gets flipped and shuffled
	 * and can be used to draw cards again.
	 */
	public void reuseDiscardedDeck () {
		//TODO send ShuffleCoding
		discardedProgrammingDeck.shuffle();
		drawProgrammingDeck = discardedProgrammingDeck;
	}

	/**
	 * This method is triggered if a robot finds itself in sight of board laser or robot laser.
	 */

	private void receiveDamage(){
		/*
		for(int i = 0; i < 2 ; i++){
			card = currentPlayer.drawDamageCard();
			programmingDeck().add(card);
		}

		 */

	}
}