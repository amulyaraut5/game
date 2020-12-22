package game.gameObjects.tiles;

import game.Player;
import Utilities.Utilities;

/**
 *
 * @author Amulya
 */

public class Antenna extends Attribute {

	private Utilities.Orientation orientation;


	/**
	 * Constructor for Antenna that basically sets the antenna facing
	 * north in the specific tile.
	 */
	Antenna(){
		this.orientation = Utilities.Orientation.UP;// means hier north

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