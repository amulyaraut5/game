package game.gameObjects.tiles;

import game.Player;
import game.gameObjects.Utilities;

import java.util.ArrayList;

/**
 *
 * @author Amulya
 */

public class BoardLaser extends Attribute {

	private Utilities.Direction direction;
	private int LaserCount;

	BoardLaser(){
		this.name = "BoardLaser";
	}

    @Override
    public void performAction(Player player) {
        determineLaserPaths();
        if(checkIfRobotIsInRange()){
            fire(player);
        }
    }



	/**
	 * It determines the path through which the lasers traverse.
	 * Lasers cannot traverse through wall, antenna and cannot
	 * penetrate more than one robot.
	 * @return return the tiles
	 */

    ArrayList<Tile> determineLaserPaths(){
        /*
        /*
        // First: Find position of laser.
        // Second: Direction at which laser is facing. This can be found from our instance variable.
        // Third: Add all horizontal or vertical tiles to  Arraylist
        // Here we should check if checkingTile is whether wall, antenna or not
        // And if the robot is standing ahead, the laser cannot traverse through robot.

        if(direction == Utilities.Direction.SOUTH){
			//TODO

        	laserAffectedTiles.add(Tile);
    		return laserAffectedTiles;
    		break;
		}
		else if(direction == Utilities.Direction.SOUTH){
			//TODO

    		laserAffectedTiles.add(Tile);
    		return laserAffectedTiles;
    		break;
		}
		else if(direction == Utilities.Direction.SOUTH){
			//TODO


    		laserAffectedTiles.add(Tile);
    		return laserAffectedTiles;
    		break;
		}
		else if(direction == Utilities.Direction.SOUTH){

    		laserAffectedTiles.add(Tile);
    		return laserAffectedTiles;
    		break;
		}
         */
		return null;

	}
	/**
	 * The lasers will only activate if it finds any robot standing in its
	 * traversing direction.
	 * @return
	 */

    private boolean checkIfRobotIsInRange() {
        /*
        // We have to check if there is robot standing or not.
        for(Tile tiles : ArrayList<Tile>){
            // Or we could use ifTileOccupied()
            if(tiles.isRobotPresent())
                return true;
        }
         */
        return true;
    }
	/**
	 * The laser will fire only if it founds any robot standing in its traversing cells.
	 * Outcome: The player will receive the spam card.
	 */

    void fire(Player player){
        /*
        // It gonna shoot the player.
        // Here we need to find the target Player.
        // targetPlayer.getRobot().getSpamCard();

     */
	}

}