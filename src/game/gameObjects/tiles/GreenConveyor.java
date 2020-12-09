package game.gameObjects.tiles;

import game.Player;
import game.gameObjects.Utilities;
/**
 *
 * @author Amulya
 */

public class GreenConveyor extends Attribute {

	private Utilities.Direction direction;
	private int speed = 1;


	@Override
	public void performAction(Player player) {
		/*
		//TODO

            First: Know the position and direction of Robot facing.
            Location location = player.getRobot().getLocation();
            Direction dir = player.getRobot().getDirection();
            Direction currentTileDir = player.getRobot().getCurrentTile.getDirection();

            Then check the whether there is collision point exist or not.
            Then we update the location of Robot in the direction of Conveyor.

            if(collisionPointExist()){
                // No movement
             else{
                // Need of location of robot and direction of tile.
                updateRobotPosition(location);
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
	 * This method updated the position of robot to new position.
	 */

	private void updateRobotCoordinates(Utilities.Direction direction){
		/*
		//TODO
		switch (direction) {
            case NORTH:
                player.setCoordinates(new Coordinate(player.getRobot().getCoordinate().getX(), player.getRobot().getCoordinate().getY() - 1));
                break;
            case SOUTH:
                player.setCoordinates(new Coordinate(player.getRobot().getCoordinate().getX(), player.getRobot().getCoordinate().getY() + 1));
                break;
            case EAST:
                player.setCoordinates(new Coordinate(player.getRobot().getCoordinate().getX() + 1, player.getRobot().getCoordinate().getY()));
                break;
            case WEST:
                player.setCoordinates(new Coordinate(player.getRobot().getCoordinate().getX() - 1, player.getRobot().getCoordinate().getY()));
                break;
        }

		 */
	}

}