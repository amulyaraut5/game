package game;

import game.gameObjects.cards.*;
import game.gameObjects.decks.ProgrammingDeck;
import game.gameObjects.robot.Robot;
import server.User;
import utilities.Utilities.Orientation;

import java.util.ArrayList;
import java.util.Map;

/**
 * This class extends user to specify different Attributes for a player.
 */

public class Player extends User {
	/**
	 * a Map which connects the register and the related card the user chooses
	 */
	private Map<Integer, Card> registerAndCards;

	private Robot robot;
	private int currentRegister;

	private Card currentAction;
	private Card lastAction;

	private int lastCheckpoint;
	private int energyCubes;

	private ProgrammingDeck drawProgrammingDeck;
	private ProgrammingDeck discardedProgrammingCards;
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

	public void setRegisterAndCards(Map<Integer, Card> mapCards){
		registerAndCards = mapCards;
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

	public ProgrammingDeck getDrawProgrammingDeck() {return drawProgrammingDeck;}

	/**
	 * It freezes the player from the current round.
	 * Effect of Pit or falling off from  the map
	 */
    public void freeze() {
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