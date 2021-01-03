package game;

import game.gameObjects.cards.DamageCard;
import game.gameObjects.cards.SpecialProgrammingCard;
import game.gameObjects.cards.UpgradeCard;
import game.gameObjects.tiles.Attribute;
import game.round.Round;

import java.util.ArrayList;

/**
 * This class handles the game itself.
 * It saves all the different assets like decks, players etc. and the game is started from here.
 */

public class Game {

	private int energyBank;
	private ArrayList<UpgradeCard> upgradeDeck;
	private ArrayList<Player> playerList;
	private Round activeRound;
	private ArrayList<DamageCard> damageCardDeck;
	private ArrayList<SpecialProgrammingCard> specialProgrammingDeck;
	private Game instance;
	private int noOfCheckpoints;

	public Game(){
		Attribute.setGame(this);
	}

	/**
	 * This methods starts Roborally.
	 */
	public void play() {
		// TODO - implement Game.play
		throw new UnsupportedOperationException();
	}

	/**
	 * TODO
	 */
	public void operation() {
		// TODO - implement Game.operation
		throw new UnsupportedOperationException();
	}

	/**
	 * TODO
	 * @return
	 */
	public int getNoOfCheckPoints() {
		return this.noOfCheckpoints;
	}
}