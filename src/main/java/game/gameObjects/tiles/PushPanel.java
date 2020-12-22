package game.gameObjects.tiles;

import game.Player;
import Utilities.Utilities;

/**
 *
 * @author Amulya
 */

public class PushPanel extends Attribute {


	private int registerValue;

	PushPanel(Utilities.Utilities.Orientation orientation, int registerValue){
		this.orientation = orientation;
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

		if(player.getCurrentRegister() == registerValue){
			updateRobotCoordinates(orientation, player);
		}
		else{
			// Do nothing
			// Print message saying that this push panel has no effect for current register.
		}


	}

	private void updateRobotCoordinates(Utilities.Orientation orientation, Player player){

		int xCoordinate = player.getRobot().getPosition().getX();
		int yCoordinate = player.getRobot().getPosition().getY();

		switch (orientation) {
			case UP:
				player.getRobot().setPosition(xCoordinate, yCoordinate - 1);
				break;
			case DOWN:
				player.getRobot().setPosition(xCoordinate, yCoordinate + 1);
				break;
			case LEFT:
				player.getRobot().setPosition(xCoordinate + 1, yCoordinate);
				break;
			case RIGHT:
				player.getRobot().setPosition(xCoordinate - 1, yCoordinate);
				break;
		}
	}
}