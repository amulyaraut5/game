package game.gameObjects.tiles;

import game.Player;

/**
 *
 * @author Amulya
 */

public class CheckPoint extends Attribute {
	private int checkPointID;

	/**
	 * Helps in creating multiple instances of Checkpoints with different ID
	 * @param id
	 */

	public CheckPoint(int id){
		this.checkPointID = id;
		this.name = "CheckPoint";
	}

	public int getCheckPointID() {
		return checkPointID;
	}

    @Override
    public void performAction(Player player) {

		/*
        First : Check how many checkpoints are there
        Second:
           if(gameBoard.getNoOfCheckPoints()==1){
                player.setWinner();
                // We can end the game and declare winner.
                gameBoard.endGame();
            }
        Third: Then we have to iterate through every checkpoints in
        order to see if the player has reached that checkpoint or not.
            else{
                int lastVisitedCheckpointID = player.getRobot().lastVisitedCheckpointID();
                // The method setLastVisitedCheckpointID() should take care of if the checkpoints are visited in order or not.
                // If the player has not visited first of the checkpoints.
                if (lastVisitedCheckpointID = 0){
                    player.getRobot().setLastVisitedCheckpointID(1);
                    // Then the checkpoint is set and player continues to play
                }
                else{
                    If the player has already visited the one of the checkpoints
                    if( lastVisitedCheckpointID < gameBoard.getNoOfCheckPoints()){
                        player.getRobot().setLastVisitedCheckPointID(lastVisitedCheckpointID + 1);
                    }
                    else if( lastVisitedCheckpointID = gameBoard.getNoOfCheckPoints()){
                        player.setWinner();
                        // We can end the game and declare winner.
                        gameBoard.endGame();
                    }
                }
            }
		*/
    }

}