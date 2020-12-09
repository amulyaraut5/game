package game.gameObjects.tiles;

import game.Player;
import game.gameObjects.Coordinate;
import game.gameObjects.Utilities;

/**
 *
 * @author Amulya
 */

public class Antenna extends Attribute {

	private Utilities.Direction direction;
	private Coordinate position;

	/**
	 * Constructor for Antenna that basically sets the antenna facing
	 * north in the specific tile.
	 */
	Antenna(){

		this.direction = Utilities.Direction.NORTH;
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