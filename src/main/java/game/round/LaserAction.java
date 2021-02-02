package game.round;

import game.Game;
import game.Player;
import game.gameObjects.maps.Map;
import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Laser;
import game.gameObjects.tiles.Wall;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;
import utilities.Coordinate;
import utilities.SoundHandler;
import utilities.enums.AttributeType;
import utilities.enums.Orientation;

import java.util.ArrayList;

/**
 * This class handles the functionality of boardLasers as well as RobotLasers.
 *
 * @author Amulya
 */

public class LaserAction {

    private static final Logger logger = LogManager.getLogger();
    private final SoundHandler soundHandler = new SoundHandler();
    private final Game game = Game.getInstance();
    private final ArrayList<Player> playerList = game.getPlayers();
    private final Map map = game.getMap();
    protected Server server = Server.getInstance();
    private ArrayList<Coordinate> laserCoordinates = new ArrayList<>();
    private ArrayList<Coordinate> robotCoordinates = new ArrayList<>();

    /**
     * Constructor for laser
     */
    public LaserAction() {
    }

    /**
     * @param position    Either robot position or position of a Laser tile.
     * @param orientation Either robot orientation or orientation of a Laser tile.
     * @param map
     * @param players
     * @return
     */
    public static Coordinate calculateLaserEnd(Coordinate position, Orientation orientation, Map map, ArrayList<Player> players) {
        position = position.clone();
        Coordinate step = orientation.toVector();

        if (isWallOnFirstTile(position, orientation, map)) return position;

        position.add(step);
        while (!position.isOutsideMap()) {
            for (Player p : players) {
                if (position.equals(p.getRobot().getCoordinate())) {
                    return position; //ends if player is on position
                }
            }
            for (Attribute a : map.getTile(position).getAttributes()) {
                if (a.getType() == AttributeType.Antenna) return position.subtract(step);
                else if (a.getType() == AttributeType.Wall) {
                    Wall wall = (Wall) a;
                    for (Orientation wallOrientation : wall.getOrientations()) {
                        if (wallOrientation == orientation.getOpposite()) { //ends if wall is crossing laser path
                            return position.subtract(step);
                        } else if (wallOrientation == orientation) {
                            return position;
                        }
                    }
                }
            }
            position.add(step);
        }
        return position.subtract(step);
    }

    private static boolean isWallOnFirstTile(Coordinate position, Orientation orientation, Map map) {
        for (Attribute a : map.getTile(position).getAttributes()) {
            if (a.getType() == AttributeType.Wall) {
                Wall wall = (Wall) a;
                for (Orientation wallOrientation : wall.getOrientations()) {
                    if (wallOrientation == orientation) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Helper function that returns a path between (and including) given coordinates.
     *
     * @param position    Position to start from.
     * @param to          Position at the end of the path.
     * @param orientation orientation to go to from the starting position.
     * @return A List containing all Coordinates between (and including) {@code position} and {@code to} or
     * an empty List if {@code to} is outside the map or not on the path in given {@code orientation}.
     */
    public static ArrayList<Coordinate> determinePath(Coordinate position, Coordinate to, Orientation orientation) {
        ArrayList<Coordinate> path = new ArrayList<>();
        Coordinate step = orientation.toVector();
        position = position.clone();

        while (!position.isOutsideMap()) {
            path.add(position.clone());
            if (position.equals(to)) return path;
            position.add(step);
        }
        return new ArrayList<>();
    }

    /**
     * This method gets triggered in activation phase.All the Board Lasers are activated at once.
     * Only the first player standing in it's way gets affected.
     */
    // TODO Delete unnecessary Logger
    public void activateBoardLaser(ArrayList<Player> activePlayers) {
        for (Coordinate coordinate : map.readLaserCoordinates()) {
            //soundHandler.pitSound();
            determineLaserPaths(coordinate);
            // TODO Check whether the lasers affect two players
            for (Coordinate coordinate1 : laserCoordinates) {
                //logger.info("BoardLaser: x:"+ coordinate1.getX() + "y:"+ coordinate1.getY());
                for (Player player : activePlayers)
                    if (player.getRobot().getCoordinate().equals(coordinate1)) {
                        game.getActivationPhase().drawDamage(game.getSpamDeck(), player, 2);
                    }
            }
        }
        laserCoordinates.clear();
    }

    /**
     * This method gets triggered after every register.
     * The player on the receiving end gets one spam card as a damage.
     * The Robot can only fire in one direction.
     * PlayerShooting Protocol is sent.
     */

    public void activateRobotLaser(ArrayList<Player> activePlayers) {
        for (Player player : activePlayers) {
            //soundHandler.pitSound();
            determineRobotLaserPath(player);
            outerLoop:
            for (Coordinate coordinate : robotCoordinates) {
                //logger.info("Robot :"+ coordinate.getX() + "y:"+ coordinate.getY());
                for (Player targetPlayer : activePlayers)
                    if (targetPlayer.getRobot().getCoordinate().equals(coordinate)) {
                        game.getActivationPhase().drawDamage(game.getSpamDeck(), targetPlayer, 1);
                        break outerLoop;
                    }
            }
        }
        robotCoordinates.clear();
    }

    /**
     * It determines the path through which the lasers traverse.
     * Lasers cannot traverse through wall, antenna and cannot
     * penetrate more than one robot.
     */

    public void determineLaserPaths(Coordinate laserPos) {
        for (Attribute a : map.getTile(laserPos).getAttributes()) {
            if (a.getType() == AttributeType.Laser) {
                Orientation orientation = ((Laser) a).getOrientation();

                Coordinate to = calculateLaserEnd(laserPos, orientation, map, game.getPlayers());
                laserCoordinates = determinePath(laserPos, to, orientation);
                /*logger.debug("BoardLaser: from " + laserPos + " to " + to);
                for (Coordinate coordinate : laserCoordinates) {
                    logger.debug("Coordinates " + coordinate);
                }*/
            }
        }
    }

    /**
     * It determines the path for Robot based on its position and direction through which the lasers traverse.
     * Lasers cannot traverse through wall, antenna and cannot
     * penetrate more than one robot.
     */

    public void determineRobotLaserPath(Player player) {
        Coordinate robotPosition = player.getRobot().getCoordinate();
        Orientation orientation = player.getRobot().getOrientation();

        Coordinate to = calculateLaserEnd(robotPosition, orientation, map, game.getPlayers());
        robotCoordinates = determinePath(robotPosition, to, orientation);
        robotCoordinates.remove(0);
        /*logger.debug("RobotLaser: from " + robotPosition + " to " + to);
        for (Coordinate coordinate : robotCoordinates) {
            logger.debug("Coordinates " + coordinate);
        }*/
    }
}
