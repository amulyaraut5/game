package game.round;

import game.Player;
import game.gameObjects.maps.MapFactory;
import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Tile;
import utilities.Coordinate;
import utilities.Utilities.Orientation;

import java.util.ArrayList;

/**
 * This class handles the functionality of boardLasers as well as RobotLasers.
 * @author Amulya
 */

public class Laser {

	ArrayList<Coordinate> coordinates = new ArrayList<>();
	ArrayList<Player> playersList = new ArrayList<>();//TODO
	/**
	 * Constructor for laser
	 */
	public Laser(){

	}

	/**
	 * The lasers will only activate if it finds any robot standing in its
	 * traversing direction.
	 * @return
	 */

	public void activateBoardLaser(){
		determineLaserPaths();
		for(Coordinate coordinate: coordinates){
			for(Player player : playersList){
				if(player.getRobot().getPosition().getX() == coordinate.getX()
				      && player.getRobot().getPosition().getY() == coordinate.getY()){
					// Then the player draws a spam card

				}
			}
			System.out.println("X:" +coordinate.getX() + "Y:" + coordinate.getY());
		}
	}

	/**
	 * The lasers will only activate if any robot is standing in the sight of another robot.
	 * @return
	 */

	public void activateRobotLaser(Player player){
		int x = player.getRobot().getPosition().getX();
		int y = player.getRobot().getPosition().getY();
		Orientation orientation = player.getDirection();

	}



	/**
	 * It determines the path through which the lasers traverse.
	 * Lasers cannot traverse through wall, antenna and cannot
	 * penetrate more than one robot.
	 *
	 */

	private void determineLaserPaths() {

	}
}