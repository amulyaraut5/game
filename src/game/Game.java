package game;

import game.gameObjects.cards.PermUpgradeCard;
import game.gameObjects.cards.ProgrammingCard;
import game.gameObjects.cards.TempUpgradeCard;
import game.gameObjects.cards.*;
import game.round.*;

import java.util.ArrayList;

public class Game {

	private int energyBank;
	private ArrayList<UpgradeCard> upgradeDeck;
	private ArrayList<Player> playerList;
	private Round activeRound;
	private ArrayList<DamageCard> damageCardDeck;
	private ArrayList<SpecialProgrammingCard> specialProgrammingDeck;
	private Game instance;

	public void play() {
		// TODO - implement Game.play
		throw new UnsupportedOperationException();
	}

	public void operation() {
		// TODO - implement Game.operation
		throw new UnsupportedOperationException();
	}

}