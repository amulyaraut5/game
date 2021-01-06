package game;

import game.gameObjects.cards.DamageCard;
import game.gameObjects.decks.ProgrammingDeck;
import game.round.Round;
import utilities.JSONProtocol.body.SelectCard;

import java.util.ArrayList;

/**
 * This class handles the game itself.
 * It saves all the different assets like decks, players etc. and the game is started from here.
 */

public class Game {

    private static Game instance;
    private int energyBank;
    private UpgradeShop upgradeShop;
    private ArrayList<Player> playerList;
    private Round activeRound;
    private ArrayList<DamageCard> damageCardDeck;
    private ProgrammingDeck specialProgrammingDeck;
    private int noOfCheckpoints;

    private Game() {
    }

    public static Game getInstance() {
        if (instance == null) instance = new Game();
        return instance;
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
     *
     * @return
     */
    public int getNoOfCheckPoints() {
        return this.noOfCheckpoints;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

}