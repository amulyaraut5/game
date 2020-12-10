package game.gameObjects.tiles;

import game.Player;
import game.gameObjects.Coordinate;
import game.gameObjects.Utilities;
/**
 *
 * @author Amulya
 */

public class GreenConveyor extends Attribute {

	private Utilities.Direction direction;
	private int speed = 1;

	/**
	 * The GreenConveyor belt pushes the robot in the direction of tile by
	 * one space.
	 * Once a robot has moved off a belt, the belt has no longer effect.
	 * @param player
	 */
	@Override
	public void performAction(Player player) {
		/*
		//TODO

            First: Know the position and direction of Robot facing.
            int xCoordinate = player.getRobot().getCoordinate().getX();
		    int yCoordinate = player.getRobot().getCoordinate().getY();

            // Second: Find the direction of robot and currentTile
            Direction dir = player.getRobot().getDirection();
            currentTileDir = player.getRobot().getCurrentTile.getDirection();

            Then check the whether there is collision point exist or not.
            Then we update the location of Robot in the direction of Conveyor.

            if(collisionPointExist()){
                // No movement
             else{
                // Need of location of robot and direction of tile.
                updateRobotCoOrdinates(xCoordinate,yCoordinate,currentTileDir,player);
             }

	*/
	}
	/**
	 * This method checks if two robots converge at the same point or not.
	 * @return
	 */

	private boolean collisionPointExist(){
		/*
		//TODO

		 */
		return true;
	}



	/**
	 * This method relocates the robot to new position based on the speed and
	 * direction of conveyor belt.
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param direction direction of conveyor belt
	 */

	private void updateRobotCoordinates(int x, int y, Utilities.Direction direction,Player player){
		switch (direction) {
            case NORTH:
                player.getRobot().setPosition(x, y - 1);
                break;
            case SOUTH:
                player.getRobot().setPosition(x, y + 1);
                break;
            case EAST:
                player.getRobot().setPosition(x + 1, y);
                break;
            case WEST:
                player.getRobot().setPosition(x - 1, y);
                break;
        }
	}

}