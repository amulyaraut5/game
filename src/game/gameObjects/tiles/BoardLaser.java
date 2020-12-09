package game.gameObjects.tiles;

public class BoardLaser extends Attribute {

	private String direction;
	private int LaserCount;

	/*
    @Override
    void performAction(Player player) {
        determineLaserPaths();
        if(checkIfRobotIsInRange()){
            fire(player);
        }
    }

     */

	/**
	 * It determines the path through which the lasers traverse.
	 * Lasers cannot traverse through wall, antenna and cannot
	 * penetrate more than one robot.
	 * @return return the tiles
	 */
    /*
    ArrayList<Tile> determineLaserPaths(){
        //TODO
        // First:
        // Second: Direction at which laser is facing.
        // Third: Add all horizontal or vertical tiles to  Arraylist
        // Here we should check if checkingTile is whether wall, antenna or not
        // And if the robot is standing ahead, the laser cannot traverse through robot.
        return ArrayList<Tile>;
    }

     */

	/**
	 * The lasers will only activate if it finds any robot standing in its
	 * traversing direction.
	 * @return
	 */
    /*
    boolean checkIfRobotIsInRange(){
        //TODO
        // Basically we have to check if there is robot standing or not.
        for(Tile tiles : ArrayList<Tile>){
            // Or we could use ifTileOccupied()
            if(tiles.isRobotPresent())
                return true;
        }
        return false;
    }
     */
	/**
	 * The laser will fire only if it founds any robot standing in its traversing cells.
	 * Outcome: The player will receive the spam card.
	 */
    /*
    void fire(Player player){
        //TODO
        // It gonna shoot the player.
        // Here we need to find the target Player.
        // targetPlayer.getRobot().getSpamCard();
    }
     */
}