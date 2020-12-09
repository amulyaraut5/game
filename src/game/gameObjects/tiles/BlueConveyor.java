package game.gameObjects.tiles;

import game.Player;
import game.gameObjects.Utilities;

/**
 *
 * @author Amulya
 */

public class BlueConveyor extends Attribute {

	private Utilities.Direction direction;
	private int speed ;

	BlueConveyor(){
		this.speed = 2;
		this.name = "BlueConveyor";
	}

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
                updateRobotCoOrdinates(xCoordinate,yCoordinate,currentTileDir);
             }
	}
	*/
	}
		/**
		 * This method checks if two robots converge at the same point or not.
		 * @return
		 */
	/*
	private boolean collisionPointExist(){
		//TODO
		return true;
	}

	 */

		/**
		 * This method updated the position of robot to new position.
		 */

	private void updateRobotCoordinates(int x, int y,Utilities.Direction direction){
		/*
		//TODO
		switch (direction) {
            case NORTH:
                player.setCoordinates(new Coordinate(x, y - 2));
                break;
            case SOUTH:
                player.setCoordinates(new Coordinate(x, y + 2));
                break;
            case EAST:
                player.setCoordinates(new Coordinate(x + 2, y));
                break;
            case WEST:
                player.setCoordinates(new Coordinate(x - 2, y));
                break;
        }
	}
	 */
	}
}
