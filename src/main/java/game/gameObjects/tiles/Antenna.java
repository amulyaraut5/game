package game.gameObjects.tiles;

import game.Player;
import utilities.Utilities;

/**
 *
 * @author Amulya
 */

public class Antenna extends Attribute {

	public Utilities.Orientation orientation;

	/**
	 * Constructor for Antenna that basically sets the antenna facing
	 * north in the specific tile.
	 */
	Antenna(){
		this.orientation = Utilities.Orientation.UP;// means here north

		this.type = "Antenna";
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