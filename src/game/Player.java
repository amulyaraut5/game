package game;

import game.gameObjects.robot.*;
import game.gameObjects.cards.*;
import server.User;

import java.util.ArrayList;

public class Player extends User {

	private Robot robot;

	private int lastCheckpoint;
	private int energyReserve;

	private ArrayList<ProgrammingCard> programmingDeck;
	private ArrayList<ProgrammingCard> discardedProgrammingCards;
	private ArrayList<ProgrammingCard> drawnProgrammingCards;
	private ArrayList<ProgrammingCard> chosenProgrammingCards;

	private ArrayList<PermUpgradeCard> installedUpgrades;
	private ArrayList<TempUpgradeCard> tempUpgradeCards;

}