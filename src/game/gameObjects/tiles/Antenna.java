package game.gameObjects.tiles;

import game.Player;
import game.gameObjects.Utilities;

/**
 *
 * @author Amulya
 */

public class Antenna extends Attribute {

	private Utilities.Direction direction;

	Antenna(){
		this.direction = Utilities.Direction.NORTH;
		this.name = "Antenna";
	}

	@Override
	public void performAction(Player player) {
		// Does Nothing
	}
}