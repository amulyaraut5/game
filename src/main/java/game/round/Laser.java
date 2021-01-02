package game.round;

import game.gameObjects.maps.Map;
import game.gameObjects.tiles.Tile;
import utilities.Coordinate;
import utilities.Utilities.Orientation;

import java.util.ArrayList;

/**
 * This class handles the functionality of boardLasers as well as RobotLasers.
 * @author Amulya
 */

public class Laser {

	private Map map;
	ArrayList<Tile> boardLaserAffectedTiles = new ArrayList<>();
	ArrayList<Tile> robotLaserAffectedTiles = new ArrayList<>();

	/**
	 * Constructor for laser
	 */
	public Laser(){

	}

	/**
	 * The lasers will only activate if it finds any robot standing in its
	 * traversing direction.
	 * @return
	 */

	public void activateBoardLaser(){
		for(Tile tile : boardLaserAffectedTiles){
			if (tile.isTileOccupied()){
				//tile.getCurrentPlayer().receiveDamage();
				// Target hit
			}
		}
	}

	/**
	 * The lasers will only activate if any robot is standing in the sight of another robot.
	 * @return
	 */

	public void activateRobotLaser(){
		for(Tile tile : robotLaserAffectedTiles){
			if (tile.isTileOccupied()){
				//
				// Target hit
			}
		}
	}

	/**
	 * This method is triggered if a robot finds itself in sight of board laser or robot laser.
	 */

	private void receiveDamage(){
		/*
		for(int i = 0; i < 2 ; i++){
			card = currentPlayer.drawDamageCard();
			programmingDeck().add(card);
		}

		 */

	}

	/**
	 * It determines the path through which the lasers traverse.
	 * Lasers cannot traverse through wall, antenna and cannot
	 * penetrate more than one robot.
	 *
	 * @return return the tiles
	 */

	private ArrayList<Tile> determineLaserPaths() {

		// Lasers tiles from the map
		for (Tile tile : map.getLaserTile()) {

			//Direction at which laser is facing.
			// Todo getAttribute() is implemented in a wrong way
			if (tile.getAttribute().getOrientation() == Orientation.DOWN)//SOUTH
			{
				// Add all  vertical tiles to  Arraylist
				int xCoordinate = map.getTilePosition(tile).getX();
				int yCoordinate = map.getTilePosition(tile).getY();
				for(int y = yCoordinate; y< 10; y++){
					// Here we should check if checkingTile is whether wall, antenna or not
					if (true){
						Tile tileAffected = map.getTile(xCoordinate, y);
						// Todo Check whether robot is standing or not
						boardLaserAffectedTiles.add(tileAffected);
						return boardLaserAffectedTiles;
					}
				}
				break;

			}
			/*else if (tile.getAttribute().getOrientation()  == Orientation.UP) {

				yCoordinate = tile.getPosition().getY();

				for(int x = yCoordinate; x> 0; x--){
					if (tile != Wall && tile != Antenna){
						laserAffectedTiles.add(tile);
						return laserAffectedTiles;
					}
				}
				break;

			} else if (tile.getAttribute().getOrientation()  == Orientation.LEFT) {
				xCoordinate = tile.getPosition().getX();
				for(int y = xCoordinate; y > 0; y --){
					if (tile != Wall && tile != Antenna){
						laserAffectedTiles.add(tile);
						return laserAffectedTiles;
					}
				}
				break;

			} else if (tile.getAttribute().getOrientation()  == Orientation.RIGHT) {
				xCoordinate = tile.getPosition().getX();

				for(int y = xCoordinate; y < 10 ; y++){
					if (tile != Wall && tile != Antenna){
						laserAffectedTiles.add(tile);
						return laserAffectedTiles;
					}
				}
				break;
			}

			 */
		}
		return null;
	}
}