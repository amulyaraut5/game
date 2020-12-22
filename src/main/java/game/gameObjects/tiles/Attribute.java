package game.gameObjects.tiles;

import game.Player;
import Utilities.Utilities;

/**
 * @author Amulya
 */

public abstract class Attribute {

	public String name;
	public Utilities.Orientation orientation;
	//public Utilities.Rotation rotation;

	/**
	 * All elements on the board must have a performAction method that can be called
	 * when the robot find itself in that specific tile.
	 * The player that is positioned on the element
	 *
	 * Another Idea : We can also Robot instead of Player.
	 */
	public abstract void performAction(Player player);

	/**
	 * Sometimes a robot may find another robot while moving in any directions.
	 * This can be used to shoot a laser or push oa
	 * @return
	 */
	public boolean checkPlayer() {
		// TODO - implement Attribute.checkPlayer
		throw new UnsupportedOperationException();
	}

	public Utilities.Orientation getOrientation() {
		return orientation;
	}
	/*
	public Utilities.Rotation getRotation() {
		return rotation;
	}

	 */

	@Override
	public String toString() {
		return "Attribute{" +
				"name='" + name + '\'' +
				'}';
	}
}