package game.gameObjects.tiles;

import game.Player;
import game.gameObjects.Utilities;

/**
 *
 * @author Amulya
 */

public class PushPanel extends Attribute {

	private Utilities.Direction direction;
	int registerValue;

	PushPanel(Utilities.Direction direction,int registerValue){
		this.direction = direction;
		this.registerValue = registerValue;
	}
	/**
	 * Push panels push any robots resting on them into the next space in the direction the push
	 * panel faces. They activate only in the register that corresponds to the number on them. For
	 * example, if you end register two on a push panel labeled “2, 4” you will be pushed. If you end
	 * register three on the same push panel, you won’t be pushed.
	 */

	@Override
	public void performAction(Player player) {
		/*
		if(player.getRegister() == registerValue){
			updateRobotCoordinates(direction);
		}
		else{
			// Do nothing
			Print message saying that this push panel has no effect for current register.
		}
		 */

	}

	private void updateRobotCoordinates(Utilities.Direction direction){
		/*
		int xCoordinate = player.getRobot().getCoordinate().getX();
		int yCoordinate = player.getRobot().getCoordinate().getY();

		switch (direction) {
			case NORTH:
				player.setCoordinates(new Coordinate(xCoordinate, yCoordinate - 1));
				break;
			case SOUTH:
				player.setCoordinates(new Coordinate(xCoordinate, yCoordinate + 1));
				break;
			case EAST:
				player.setCoordinates(new Coordinate(xCoordinate + 1, yCoordinate));
				break;
			case WEST:
				player.setCoordinates(new Coordinate(xCoordinate - 1, yCoordinate));
				break;
		}
		 */
	}
}