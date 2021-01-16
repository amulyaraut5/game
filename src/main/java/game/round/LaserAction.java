package game.round;

import game.Game;
import game.Player;
import game.gameObjects.maps.Map;
import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Laser;
import game.gameObjects.tiles.Tile;
import game.gameObjects.tiles.Wall;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Coordinate;
import utilities.Orientation;
import utilities.Utilities.AttributeType;

import java.util.ArrayList;

/**
 * This class handles the functionality of boardLasers as well as RobotLasers.
 *
 * @author Amulya
 */

public class LaserAction {

    private static final Logger logger = LogManager.getLogger();
    ArrayList<Coordinate> coordinates = new ArrayList<>();
    ArrayList<Coordinate> roboCoordinates = new ArrayList<>();
    Game game = Game.getInstance();
    ArrayList<Player> playerList = game.getPlayers();
    Map map = game.getMap();

    /**
     * Constructor for laser
     */
    public LaserAction() {
    }

    /**
     * The effect of lasers will be seen if it finds any robot standing in its
     * traversing direction.
     */

    public void activateBoardLaser() {
        determineLaserPaths();
        for (Coordinate coordinate : coordinates) {
            for (Player player : playerList) {
                if (player.getRobot().getPosition().getX() == coordinate.getX()
                        && player.getRobot().getPosition().getY() == coordinate.getY()) {
                    //player.getDiscardedProgrammingDeck().addSpamCard();

                }
            }
        }
    }

    /**
     * This method gets triggered after every register.
     * The player on the receiving end gets one spam card as a damage.
     *
     * @param currentPlayer
     */

    public void activateRobotLaser(Player currentPlayer) {
        determineRobotLaserPath(currentPlayer);
        for (Coordinate coordinate : roboCoordinates) {
            for (Player targetPlayer : playerList) {
                if (currentPlayer != targetPlayer) { //TODO

                    if (targetPlayer.getRobot().getPosition().getX() == coordinate.getX()
                            && targetPlayer.getRobot().getPosition().getY() == coordinate.getY()) {
                        //targetPlayer.getDiscardedProgrammingDeck().addSpamCard();

                    }
                }
            }
        }
    }

    /**
     * It determines the path through which the lasers traverse.
     * Lasers cannot traverse through wall, antenna and cannot
     * penetrate more than one robot.
     */
    //TODO Antenna Case and Delete unnecessary logger.info
    public void determineLaserPaths() {
        for (Coordinate coordinate : map.getLaserCoordinates()) {
            int xC = coordinate.getX();
            int yC = coordinate.getY();

            Tile tile = map.getTile(xC, yC);
            logger.info("X" + xC + "y" + yC);

            for (Attribute a : tile.getAttributes()) {
                if (a.getType() == AttributeType.Laser) {
                    Orientation orientation = ((Laser) a).getOrientation();
                    coordinates = determinePath(orientation, coordinate); //TODO "coordinates =" or "coordinates.addAll()"
                }
            }
        }
    }

    /**
     * It determines the path for Robot based on its position and direction through which the lasers traverse.
     * Lasers cannot traverse through wall, antenna and cannot
     * penetrate more than one robot.
     */
    // TODO //TODO Antenna Case and Delete unnecessary logger.info
    public void determineRobotLaserPath(Player player) {
        Orientation orientation = player.getRobot().getOrientation();
        Coordinate robotPosition = player.getRobot().getPosition();

        roboCoordinates = determinePath(orientation, robotPosition); //TODO "roboCoordinates =" or "roboCoordinates.addAll()"
    }

    private ArrayList<Coordinate> determinePath(Orientation orientation, Coordinate position) {
        ArrayList<Coordinate> path = new ArrayList<>();
        position = position.clone();
        Coordinate step = orientation.toVector();

        outerLoop:
        while (position.getX() >= 0 || position.getX() <= 10 || position.getY() >= 0 || position.getY() <= 10) {
            position.add(step);
            Tile affectedTile = map.getTile(position.getX(), position.getY());
            for (Attribute b : affectedTile.getAttributes()) {
                if (b.getType() != AttributeType.Wall) { //TODO test if there is a Antenna, robot
                    path.add(position.clone());
                    logger.info("Laser Affected Coordinate:" + "(x,y) =" + "(" + position.getX() + "," + position.getY() + ")");
                    break; //TODO what is if at the next attribute is a Wall?
                } else if (b.getType() == AttributeType.Wall) {
                    if (((Wall) b).getOrientation() == orientation) {  //TODO test if wall orientation is opposite of robot orientation
                        path.add(position.clone());
                        logger.info("Laser Affected Coordinate:" + "(x,y) =" + "(" + position.getX() + "," + position.getY() + ")");
                        break outerLoop;
                    }
                }
            }
        }
        return path;
    }
}
