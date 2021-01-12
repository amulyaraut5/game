package game;

import game.gameObjects.cards.DamageCard;
import game.gameObjects.decks.ProgrammingDeck;
import game.gameObjects.decks.SpamDeck;
import game.gameObjects.decks.VirusDeck;
import game.gameObjects.maps.Map;
import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Tile;
import game.round.Round;
import utilities.Coordinate;
import utilities.Utilities;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class handles the game itself.
 * It saves all the different assets like decks, players etc. and the game is started from here.
 */

public class Game {

    private static Game instance;
    private int energyBank;
    private UpgradeShop upgradeShop;
    private ArrayList<Player> playerList;
    private HashMap<Integer, Player> playerIDs = new HashMap<>();
    private Round activeRound;
    private SpamDeck spamDeck;
    private VirusDeck virusDeck;
    private ProgrammingDeck programmingDeck;
    private ArrayList<DamageCard> damageCardDeck;
    private ProgrammingDeck specialProgrammingDeck;
    private int noOfCheckpoints;
    private Map map;
    private ArrayList<Player> players;

    private Game() {
    }

    public static Game getInstance() {
        if (instance == null) instance = new Game();
        return instance;
    }

    //TODO resetGame()

    /**
     * This methods starts Roborally.
     */
    public void play() {
        // TODO - implement Game.play
        throw new UnsupportedOperationException();
    }

    public Map getMap() {
        return map;
    }

    /**
     * TODO
     *
     * @return
     */
    public int getNoOfCheckPoints() {
        return this.noOfCheckpoints;
    }


    //private void activateBelts(){
    //    ArrayList<Coordinate> coordinates = getBeltCoordinates(map);
    //    for(Player player : players){
    //        for(Coordinate c : coordinates){
    //            if((player.getRobot().getPosition().getX() == c.getX()) && (player.getRobot().getPosition().getY() == c.getY())){
    //
    //            }
    //        }
    //    }
    //
    //
    //
    //}



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