package game.gameObjects.tiles;

import game.Player;
import game.gameObjects.Coordinate;
import Utilities.Utilities;

/**
 *
 * @author Amulya
 */

public class Antenna extends Attribute {

	private Utilities.Orientation direction;
	private Coordinate position;

	/**
	 * Constructor for Antenna that basically sets the antenna facing
	 * north in the specific tile.
	 */
	Antenna(){
		this.direction = Utilities.Orientation.UP;// means hier north
		// TODO change x and y coordinate
		this.position = new Coordinate(4,5);

		this.name = "Antenna";
	}

	/**
	 * Antenna does not really have a function besides it act as
	 * shield and cannot be moved by any robot.
	 * @param player
	 */
	@Override
	public void performAction(Player player) {
		// Does Nothing
	}
}