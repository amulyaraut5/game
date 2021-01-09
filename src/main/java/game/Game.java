package game;

import game.gameObjects.cards.DamageCard;
import game.gameObjects.decks.ProgrammingDeck;
import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Tile;
import game.round.Round;
import utilities.Coordinate;
import utilities.JSONProtocol.body.SelectCard;
import utilities.JSONProtocol.body.gameStarted.BoardElement;
import utilities.Utilities;

import java.lang.reflect.Array;
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
    private Tile[][] map;
    private ArrayList<Player> players;

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

    public ArrayList<Coordinate> getLaserCoordinates(Tile[][] map){
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < (map.length); i++) {
            for (int j = 0; j < (map[0].length); j++) {
                for(Attribute a : map[i][j].getAttributes()){
                    if(a.getType() == Utilities.AttributeType.Laser){
                        Coordinate temp = new Coordinate(i,j);
                        coordinates.add(temp);
                    }
                }
            }
        }
    return coordinates;
    }

    public ArrayList<Coordinate> getBeltCoordinates(Tile[][] map){
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < (map.length); i++) {
            for (int j = 0; j < (map[0].length); j++) {
                for(Attribute a : map[i][j].getAttributes()){
                    if(a.getType() == Utilities.AttributeType.Belt || a.getType() == Utilities.AttributeType.RotatingBelt){
                        Coordinate temp = new Coordinate(i,j);
                        coordinates.add(temp);
                    }
                }
            }
        }
        return coordinates;
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

}