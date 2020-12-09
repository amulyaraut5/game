package game.gameObjects.tiles;

import game.Player;
import game.gameObjects.Utilities;

/**
 *
 * @author Amulya
 */

public class Gear extends Attribute {

	private Utilities.Rotation rotation;

	// While instantiating the object we have to mention the direction of rotation.
	public Gear(Utilities.Rotation rotation){
		this.name = "Gear";
		this.rotation = rotation;
	}

	/**
	 * Rotates the robot in anti-clockwise direction and clock wise direction.
	 * Direction is initialized by constructor.
	 * @param player
	 *
	 */

	@Override
	public void performAction(Player player) {
        /*
            First: We need the direction at which the gear rotates
            Second: We need the direction of Robot.
            switch(rotation) {
                case RIGHT:
                switch (player.getRobot().getDirection()) {
                    case NORTH:
                        player.getRobot().setDirection(Utilities.Direction.EAST);
                        break;
                    case EAST:
                        player.getRobot().setDirection(Utilities.Direction.SOUTH);
                        break;
                    case SOUTH:
                        player.getRobot().setDirection(Utilities.Direction.WEST);
                        break;
                    case WEST:
                        player.getRobot().setDirection(Utilities.Direction.NORTH);
                }
                break;
                case LEFT:
                switch (player.getRobot().getDirection()) {
                    case NORTH:
                        player.getRobot().setDirection(Utilities.Direction.WEST);
                        break;
                    case WEST:
                        player.getRobot().setDirection(Utilities.Direction.SOUTH);
                        break;
                    case SOUTH:
                        player.getRobot().setDirection(Utilities.Direction.EAST);
                        break;
                    case EAST:
                        player.getRobot().setDirection(Utilities.Direction.NORTH);
                        break;
                }
                break;
	*/
	}

}