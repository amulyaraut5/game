package game.round;

import game.Player;
import game.gameObjects.maps.MapFactory;
import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Tile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Coordinate;
import utilities.Orientation;

import java.util.ArrayList;

/**
 * This class handles the functionality of boardLasers as well as RobotLasers.
 *
 * @author Amulya
 */

public class Laser {

    private static final Logger logger = LogManager.getLogger();
    ArrayList<Coordinate> coordinates = new ArrayList<>();
    ArrayList<Coordinate> roboCoordinates = new ArrayList<>();
    ArrayList<Player> playersList = new ArrayList<>();//TODO Get active Playerlist from Game

    /**
     * Constructor for laser
     */
    public Laser() {
    }

    /**
     * The effect of lasers will be seen if it finds any robot standing in its
     * traversing direction.
     */

    public void activateBoardLaser() {
        determineLaserPaths();
        for (Coordinate coordinate : coordinates) {
            for (Player player : playersList) {
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
            for (Player targetPlayer : playersList) {
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
    private void determineLaserPaths() {
        for (Coordinate coordinate : MapFactory.getInstance().getLaserCoordinates()) {
            int xC = coordinate.getX();
            int yC = coordinate.getY();

            Tile tile = MapFactory.getInstance().getTile(xC, yC);

            for (Attribute a : tile.getAttributes()) {
                if (a.getType() == "Laser") {//TODO use equals()
                    Orientation orientation = a.getOrientation();

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
            Tile affectedTile = MapFactory.getInstance().getTile(position.getX(), position.getY());
            for (Attribute b : affectedTile.getAttributes()) {
                if (b.getType() != "Wall") { //TODO test if there is a Antenna, robot
                    path.add(position.clone());
                    logger.info("Laser Affected Coordinate:" + "(x,y) =" + "(" + position.getX() + "," + position.getY() + ")");
                    break; //TODO what is if at the next attribute is a Wall?
                } else if (b.getType() == "Wall") {
                    if (b.getOrientation() == orientation) {  //TODO test if wall orientation is opposite of robot orientation
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
