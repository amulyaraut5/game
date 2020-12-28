package game.gameObjects.tiles;

import game.Player;
import utilities.Utilities.Orientation;

/**
 *
 * @author Amulya
 */

public class Belt extends Attribute {

	// Orientation in this context means the direction in which
	// belt pushes the robot.


	// speed for belt 1-Blue Conveyor 2- Green Conveyor

	private int speed;

	Belt(Orientation orientation, int speed){
		this.speed = speed;
		this.type = "Belt";
		this.orientation = orientation;
	}

	/**
	 * The GreenConveyor belt pushes the robot in the direction of tile by
	 * one space.
	 * Once a robot has moved off a belt, the belt has no longer effect.
	 * @param player
	 */
	@Override
	public void performAction(Player player) {

		int xCoordinate = player.getRobot().getPosition().getX();
		int yCoordinate = player.getRobot().getPosition().getY();

		Orientation orientation = player.getRobot().getOrientation();
		// Conveyor does not really change the direction of robot  unless it is rotating
		// belt.Handled separately.

		// Then check the whether there is collision point exist or not.
		// Then we update the location of Robot in the direction of Conveyor.

            if(collisionPointExist()) {
				// No movement
			}else{
                // Need of location of robot and direction of tile.
				updateRobotCoordinates(xCoordinate,yCoordinate,player);
             }
	}

	/**
	 * This method checks if two robots converge at the same point or not.
	 * @return
	 */

	 private boolean collisionPointExist(){
		//TODO
		return false;
	}



	/**
	 * This method relocates the robot to new position based on the speed and
	 * direction of conveyor belt.
	 * @param x x coordinate
	 * @param y y coordinate
	 */

	private void updateRobotCoordinates(int x, int y,Player player){
		switch (orientation) {
			case UP: //NORTH
				player.getRobot().setPosition(x, y - 1);
				break;
			case DOWN: //SOUTH
				player.getRobot().setPosition(x, y + 1);
				break;
			case LEFT: //EAST
				player.getRobot().setPosition(x + 1, y);
				break;
			case RIGHT: //WEST
				player.getRobot().setPosition(x - 1, y);
				break;
		}
	}
}
