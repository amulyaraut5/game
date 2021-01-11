package game;

import game.gameObjects.cards.DamageCard;
import game.gameObjects.decks.ProgrammingDeck;
import game.gameObjects.decks.SpamDeck;
import game.gameObjects.decks.VirusDeck;
import game.gameObjects.maps.Map;
import game.round.Round;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class handles the game itself.
 * It saves all the different assets like decks, players etc. and the game is started from here.
 */

public class Game {
    private static Game instance;

    private ArrayList<Player> playerList;
    private HashMap<Integer, Player> playerIDs = new HashMap<>();
    private UpgradeShop upgradeShop;

    private Round activeRound;
    private Map map;

    private int noOfCheckpoints;
    private int energyBank;

    private SpamDeck spamDeck;
    private VirusDeck virusDeck;
    private ProgrammingDeck programmingDeck;
    private ArrayList<DamageCard> damageCardDeck;
    private ProgrammingDeck specialProgrammingDeck;

    private Game() {
    }

    public static Game getInstance() {
        if (instance == null) instance = new Game();
        return instance;
    }

    public Map getMap() {
        return map;
    }

    /**
     * This methods starts Roborally.
     */
    public void play() {
        // TODO - implement Game.play
        throw new UnsupportedOperationException();
    }

    public int getNoOfCheckPoints() {
        return this.noOfCheckpoints;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public SpamDeck getSpamDeck() {
        return spamDeck;
    }

    public VirusDeck getVirusDeck() {
        return virusDeck;
    }

    public ProgrammingDeck getProgrammingDeck() {
        return programmingDeck;
    }

    public Round getActiveRound() {
        return activeRound;
    }

    public Player getPlayerFromID (Integer id) { return playerIDs.get(id);}
}