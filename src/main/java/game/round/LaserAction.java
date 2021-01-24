package game.round;

import game.Game;
import game.Player;
import game.gameObjects.cards.Card;
import game.gameObjects.maps.Map;
import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Laser;
import game.gameObjects.tiles.Wall;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;
import utilities.Coordinate;
import utilities.JSONProtocol.body.DrawDamage;
import utilities.JSONProtocol.body.PlayerShooting;
import utilities.SoundHandler;
import utilities.enums.AttributeType;
import utilities.enums.CardType;
import utilities.enums.Orientation;

import java.util.ArrayList;

/**
 * This class handles the functionality of boardLasers as well as RobotLasers.
 *
 * @author Amulya
 */

public class LaserAction {

    private static final Logger logger = LogManager.getLogger();
    private SoundHandler soundHandler = new SoundHandler();
    protected Server server = Server.getInstance();
    private Game game = Game.getInstance();

    private ArrayList<Coordinate> laserCoordinates = new ArrayList<>();
    private ArrayList<Coordinate> robotCoordinates = new ArrayList<>();

    private ArrayList<Player> playerList = game.getPlayers();
    private Map map = game.getMap();


    /**
     * Constructor for laser
     */
    public LaserAction() { }

    /**
     *  This method gets triggered in activation phase.All the Board Lasers are activated at once.
     *  Only the first player standing in it's way gets affected.
     */

    public void activateBoardLaser() {
        //soundHandler.pitSound();
        server.communicateAll(new PlayerShooting());
        determineLaserPaths();
        // TODO Check whether the lasers affect two players
        for (Coordinate coordinate : laserCoordinates) {
            for (Player player : playerList)
                if (player.getRobot().getCoordinate() == coordinate) receiveDamage(player);
            break;
        }
    }

    /**
     * This method gets triggered after every register.
     * The player on the receiving end gets one spam card as a damage.
     * The Robot can only fire in one direction.
     * PlayerShooting Protocol is sent.
     */

    public void activateRobotLaser() {
        //soundHandler.pitSound();
        server.communicateAll(new PlayerShooting());
        determineRobotLaserPath();
        for (Coordinate coordinate : robotCoordinates) {
            for (Player targetPlayer : playerList)
                if (targetPlayer.getRobot().getCoordinate() == coordinate) receiveDamage(targetPlayer);
            break;
        }

    }

    /**
     * This method handles the afterEffect of being shot by Laser.
     * Players receive spam card and adds to the discarded programming deck.
     * The number of damage received depends on laser count.
     * Draw Damage Protocol is sent to all players.
     * @param player
     */
    private void receiveDamage(Player player){
        ArrayList<Card> spamCard = game.getSpamDeck().drawCards(1);
        ArrayList<CardType> cardType = new ArrayList<>();
        for(Card card : spamCard){
            player.getDiscardedProgrammingDeck().addCard(card);
            cardType.add(CardType.Spam);
        }
        server.communicateAll(new DrawDamage(player.getID(), cardType));
    }

    /**
     * It determines the path through which the lasers traverse.
     * Lasers cannot traverse through wall, antenna and cannot
     * penetrate more than one robot.
     */

    public void determineLaserPaths() {
        for (Coordinate coordinate : map.readLaserCoordinates()) {
            int xC = coordinate.getX();
            int yC = coordinate.getY();
            for (Attribute a : map.getTile(xC, yC).getAttributes()) {
                if (a.getType() == AttributeType.Laser) {
                    Orientation orientation = ((Laser) a).getOrientation();
                    laserCoordinates = determinePath(orientation, coordinate);
                    /*for(Coordinate coordinate1: laserCoordinates){
                        logger.info("Laser Affected Coordinate:" + "(x,y) =" + "(" + coordinate1.getX() + "," + coordinate1.getY() + ")");
                    }*/
                }
            }
        }

    }

    /**
     * It determines the path for Robot based on its position and direction through which the lasers traverse.
     * Lasers cannot traverse through wall, antenna and cannot
     * penetrate more than one robot.
     * */

    public void determineRobotLaserPath() {
        for(Player player: playerList){
            Orientation orientation = player.getRobot().getOrientation();
            Coordinate robotPosition = player.getRobot().getCoordinate();
            robotCoordinates = determinePath(orientation, robotPosition);
            robotCoordinates.remove(0);
        }
    }


    /**
     * Helper function that returns the arraylist of laser affected coordinates.
     * @param orientation  Either robot orientation or orientation of tile, depends on who calls this method
     * @param position Either robot position or position of tile in map, depends on who calls this method
     * @return
     */
    private ArrayList<Coordinate> determinePath(Orientation orientation, Coordinate position) {
        ArrayList<Coordinate> path = new ArrayList<>();
        path.add(position);
        position = position.clone();
        Coordinate step = orientation.toVector();

        outerLoop:
        while (true) {
            position.add(step);
            if(position.isOutOfBound()){
                logger.info("Laser Out of Bound"); break outerLoop;
            }
            else{
                for (Attribute b : map.getTile(position.getX(), position.getY()).getAttributes()) {
                    if (b.getType() != AttributeType.Wall && b.getType() != AttributeType.Antenna && b.getType() != AttributeType.Laser
                            && b.getType() != AttributeType.ControlPoint) {
                        path.add(position.clone());
                        break;
                    }else if (b.getType() == AttributeType.ControlPoint && b.getType() == AttributeType.Laser) {
                        path.add(position.clone()); break outerLoop;
                    }
                    else if(b.getType() == AttributeType.Laser) {
                        if (((Laser) b).getOrientation() == orientation) {
                            break outerLoop;
                        } else if (((Laser) b).getOrientation() == orientation.getOpposite()) {
                            path.add(position.clone());
                            break outerLoop;
                        } else if (((Laser) b).getOrientation() != orientation) {
                            path.add(position.clone());
                            break;
                        }
                    }
                    else if (b.getType() == AttributeType.Wall) {
                        if (((Wall) b).getOrientation() == orientation) {
                            path.add(position.clone());
                            break outerLoop;
                        }else if(((Wall) b).getOrientation() == orientation.getOpposite()){
                            break outerLoop;
                        }
                        else if (((Wall) b).getOrientation() != orientation){
                            path.add(position.clone());break;
                        }
                    }else if (b.getType() == AttributeType.Antenna) break outerLoop;
                }
            }
        }
        return path;
    }
}
