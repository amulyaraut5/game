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
    private ArrayList<Coordinate> coordinates = new ArrayList<>();
    private ArrayList<Coordinate> roboCoordinates = new ArrayList<>();
    private Game game = Game.getInstance();
    private ArrayList<Player> playerList = game.getPlayers();
    private Map map = game.getMap();
    private SoundHandler soundHandler = new SoundHandler();

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
        determineLaserPaths();
        // TODO Check whether the lasers affect two players
        for (Coordinate coordinate : coordinates) {
            for (Player player : playerList)
                if (player.getRobot().getCoordinate() == coordinate) receiveDamage(player);
            break;
        }
    }

    /**
     * This method gets triggered after every register.
     * The player on the receiving end gets one spam card as a damage.
     * The Robot can only fire in one direction.
     * @param currentPlayer
     */

    public void activateRobotLaser(Player currentPlayer) {
        //soundHandler.pitSound();
        determineRobotLaserPath(currentPlayer);
        for (Coordinate coordinate : roboCoordinates) {
            for (Player targetPlayer : playerList) {
                if (currentPlayer.getID() != targetPlayer.getID())
                    if (targetPlayer.getRobot().getCoordinate() == coordinate) receiveDamage(targetPlayer);
            }
        }
    }

    // TODO player receive the spam cards and add in the discarded pile
    private void receiveDamage(Player player){
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
            System.out.println("x"+xC + "y"+yC);
            for (Attribute a : map.getTile(xC, yC).getAttributes()) {
                if (a.getType() == AttributeType.Laser) {
                    Orientation orientation = ((Laser) a).getOrientation();
                    coordinates = determinePath(orientation, coordinate);
                    for(Coordinate coordinate1: coordinates){
                        logger.info("Laser Affected Coordinate:" + "(x,y) =" + "(" + coordinate1.getX() + "," + coordinate1.getY() + ")");
                    }
                }
            }
        }
    }

    /**
     * It determines the path for Robot based on its position and direction through which the lasers traverse.
     * Lasers cannot traverse through wall, antenna and cannot
     * penetrate more than one robot.
     */

    public void determineRobotLaserPath(Player player) {
        Orientation orientation = player.getRobot().getOrientation();
        Coordinate robotPosition = player.getRobot().getCoordinate();
        roboCoordinates = determinePath(orientation, robotPosition);
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
        while ((position.getX() >= 0 && position.getX() < 13) && (position.getY() >= 0 && position.getY() < 10)) {
            position.add(step);
            for (Attribute b : map.getTile(position.getX(), position.getY()).getAttributes()) {
                if (b.getType() != AttributeType.Wall && b.getType() != AttributeType.Antenna && b.getType() != AttributeType.Laser
                        && b.getType() != AttributeType.ControlPoint) {
                    path.add(position.clone());
                    break;
                } else if (b.getType() == AttributeType.Wall) {
                    if (((Wall) b).getOrientation() == orientation) {
                        path.add(position.clone());
                        break outerLoop;
                    }else if (((Wall) b).getOrientation() != orientation){
                        path.add(position.clone());
                        break outerLoop;
                    }
                }else if (b.getType() == AttributeType.Antenna) break outerLoop;
                else if(b.getType() == AttributeType.Laser){
                    if (((Laser) b).getOrientation() == orientation) {
                        path.add(position.clone());
                        break outerLoop;
                    }else if (((Laser) b).getOrientation() != orientation){
                        path.add(position.clone());
                        break outerLoop;
                    }
                }else if (b.getType() == AttributeType.ControlPoint) {
                    path.add(position.clone());break outerLoop;
                }
            }
        }
        return path;
    }
}
