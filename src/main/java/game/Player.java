package game;

import game.gameObjects.robot.*;
import game.gameObjects.cards.*;
import server.User;

import java.util.ArrayList;
import java.util.Map;

public class Player extends User {
	/**
	 * a Map which connects the register and the related card the user chooses
	 */
	private Map<Integer, Card> registerAndCards;



	private Robot robot;
	private int currentRegister;

	private int lastCheckpoint;
	private int energyReserve;

	private ArrayList<ProgrammingCard> programmingDeck;
	private ArrayList<ProgrammingCard> discardedProgrammingCards;
	private ArrayList<ProgrammingCard> drawnProgrammingCards;
	private ArrayList<ProgrammingCard> chosenProgrammingCards;

	private ArrayList<PermUpgradeCard> installedUpgrades;
	private ArrayList<TempUpgradeCard> tempUpgradeCards;

	private int checkPointCounter;

	public void setRegisterAndCards(Map<Integer, Card> mapCards){
		registerAndCards = mapCards;
	}


	public Robot getRobot(){
		return this.robot;
	}

	public int getCurrentRegister(){ return this.currentRegister; }

	public int getCheckPointCounter() {
		return checkPointCounter;
	}

	public void setCheckPointCounter(int checkPointCounter) {
		this.checkPointCounter = checkPointCounter;
	}

	public int getEnergyReserve() {
		return energyReserve;
	}

	public void setEnergyReserve(int energyReserve) {
		this.energyReserve = energyReserve;
	}

	/**
	 * It freezes the player from the current round.
	 * Effect of Pit or falling off from  the map
	 */
    public void freeze() {
    }
}