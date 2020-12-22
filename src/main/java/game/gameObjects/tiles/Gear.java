package game.gameObjects.tiles;

import game.Player;
import Utilities.Utilities;

/**
 *
 * @author Amulya
 */

public class Gear extends Attribute {

	// Orientation from Gear should be interpreted as Rotation
	// Clockwise--RIGHT Anticlockwise--LEFT

	public Gear(Utilities.Orientation orientation){
		this.type = "Gear";
		this.orientation = orientation;
	}

	/**
	 * Rotates the robot in anti-clockwise direction and clock wise direction.
	 * Orientation is initialized by constructor.
	 * @param player
	 */

	@Override
	public void performAction(Player player) {

		switch(orientation) {
			case RIGHT:
				switch (player.getRobot().getOrientation()) {
					case UP: //NORTH
						player.getRobot().setOrientation(Utilities.Orientation.LEFT);
						break;
					case LEFT: //EAST
						player.getRobot().setOrientation(Utilities.Orientation.DOWN);
						break;
					case DOWN: //SOUTH
						player.getRobot().setOrientation(Utilities.Orientation.RIGHT);
						break;
					case RIGHT: //WEST
						player.getRobot().setOrientation(Utilities.Orientation.UP);
				}
				break;
			case LEFT:
				switch (player.getRobot().getOrientation()) {
					case UP: //NORTH
						player.getRobot().setOrientation(Utilities.Orientation.RIGHT);
						break;
					case LEFT: //EAST
						player.getRobot().setOrientation(Utilities.Orientation.DOWN);
						break;
					case DOWN: //SOUTH
						player.getRobot().setOrientation(Utilities.Orientation.LEFT);
						break;
					case RIGHT: //WEST
						player.getRobot().setOrientation(Utilities.Orientation.UP);
						break;
				}
				break;
		}
	}

}