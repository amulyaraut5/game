package game.round;

import game.Player;
import game.gameObjects.maps.MapFactory;
import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Tile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Coordinate;
import utilities.Orientation;

import java.util.ArrayList;

/**
 * This class handles the functionality of boardLasers as well as RobotLasers.
 * @author Amulya
 */

public class Laser {

	ArrayList<Coordinate> coordinates = new ArrayList<>();
	ArrayList<Coordinate> roboCoordinates = new ArrayList<>();
	ArrayList<Player> playersList = new ArrayList<>();//TODO Get active Playerlist from Game
	private static final Logger logger = LogManager.getLogger();
	/**
	 * Constructor for laser
	 */
	public Laser(){
	}

	/**
	 * The effect of lasers will be seen if it finds any robot standing in its
	 * traversing direction.
	 */

	public void activateBoardLaser(){
		determineLaserPaths();
		for(Coordinate coordinate: coordinates){
			for(Player player : playersList){
				if(player.getRobot().getPosition().getX() == coordinate.getX()
				      && player.getRobot().getPosition().getY() == coordinate.getY()){
					 //player.getDiscardedProgrammingDeck().addSpamCard();

				}
			}
		}
	}
	/**
	 * This method gets triggered after every register.
	 * The player on the receiving end gets one spam card as a damage.
	 * @param currentPlayer
	 */

	public void activateRobotLaser(Player currentPlayer){
		determineRobotLaserPath(currentPlayer);
		for(Coordinate coordinate: roboCoordinates){
			for(Player targetPlayer : playersList){
				if(currentPlayer != targetPlayer){ //TODO

					if(targetPlayer.getRobot().getPosition().getX() == coordinate.getX()
							&& targetPlayer.getRobot().getPosition().getY() == coordinate.getY()){
						//targetPlayer.getDiscardedProgrammingDeck().addSpamCard();

					}
				}
			}
		}
	}

	/**
	 * It determines the path through which the lasers traverse.
	 * Lasers cannot traverse through wall, antenna and cannot
	 * penetrate more than one robot.
	 */
	//TODO Antenna Case and Delete unnecessary logger.info

	private void determineLaserPaths() {
		for (Coordinate coordinate : MapFactory.getInstance().getLaserCoordinates()) {
			int xC = coordinate.getX();
			int yC = coordinate.getY();

			Tile tile = MapFactory.getInstance().getTile(xC, yC);

			for (Attribute a : tile.getAttributes()) {
				if (a.getType() == "Laser") {
					Orientation orientation = a.getOrientation();
					outerLoop:
					switch(orientation){
						case RIGHT: logger.info("Orientation of Laser is Right at Coordinate" + "("+ xC + ","+ yC+")" );
							for (int y = yC; y < 10; y++) {
								Tile tileAffected = MapFactory.getInstance().getTile(xC, y);
								for(Attribute b: tileAffected.getAttributes()){
									if(b.getType() != "Wall" ){
										coordinates.add(new Coordinate(xC,y));
										logger.info("Laser Affected Coordinate:" + "(x,y) =" + "("+ xC + ","+ y+")");
										break;
									}
									else if (b.getType() == "Wall"){
										if(b.getOrientation() == Orientation.RIGHT){
											coordinates.add(new Coordinate(xC,y));
											logger.info("Laser Affected Coordinate:" + "(x,y) =" + "("+ xC + ","+ y+")");
											break outerLoop;
										}
									}
								}
							}
						case LEFT: logger.info("Orientation of Laser is LEFT at Coordinate" + "("+ xC + ","+ yC+")" );
							for (int y = yC; y >= 0; y--) {
								Tile tileAffected = MapFactory.getInstance().getTile(xC, y);
								for(Attribute b: tileAffected.getAttributes()){
									if(b.getType() != "Wall" ){
										coordinates.add(new Coordinate(xC, y));
										logger.info("Laser Affected Coordinate:" + "(x,y) =" + "("+ xC + ","+ y+")");
										break;
									}
									else if (b.getType() == "Wall"){
										if(b.getOrientation() == Orientation.LEFT){
											coordinates.add(new Coordinate(xC,y));
											logger.info("Laser Affected Coordinate:" + "(x,y) =" + "("+ xC + ","+ y+")");
											break outerLoop;
										}
									}
								}
							}
						case DOWN: logger.info("Orientation of Laser is DOWN at Coordinate" + "("+ xC + ","+ yC+")" );
							for (int x = xC; x < 10; x++) {
								Tile tileAffected = MapFactory.getInstance().getTile(x, yC);
								for(Attribute b: tileAffected.getAttributes()){
									if(b.getType() != "Wall" ){
										coordinates.add(new Coordinate(x, yC));
										logger.info("Laser Affected Coordinate:" + "(x,y) =" + "("+ x + ","+ yC+")");
										break;
									}
									else if (b.getType() == "Wall"){
										if(b.getOrientation() == Orientation.DOWN){
											coordinates.add(new Coordinate(x,yC));
											logger.info("Laser Affected Coordinate:" + "(x,y) =" + "("+ x + ","+ yC+")");
											break outerLoop;
										}
									}
								}

							}
						case UP: logger.info("Orientation of Laser is UP at Coordinate" + "("+ xC + ","+ yC+")" );
							for (int x = xC; x >= 0; x--) {
								Tile tileAffected = MapFactory.getInstance().getTile(x, yC);
								for(Attribute b: tileAffected.getAttributes()){
									if(b.getType() != "Wall" ){
										coordinates.add(new Coordinate(x, yC));
										logger.info("Laser Affected Coordinate:" + "(x,y) =" + "("+ x + ","+ yC+")");
										break;
									}
									else if (b.getType() == "Wall"){
										if(b.getOrientation() == Orientation.UP){
											coordinates.add(new Coordinate(x,yC));
											logger.info("Laser Affected Coordinate:" + "(x,y) =" + "("+ x + ","+ yC+")");
											break outerLoop;
										}
									}
								}
							}
					}
				}
			}
		}
	}


	/**
	 * It determines the path for Robot based on its position and direction through which the lasers traverse.
	 * Lasers cannot traverse through wall, antenna and cannot
	 * penetrate more than one robot.
	 */
	// TODO //TODO Antenna Case and Delete unnecessary logger.info

	public void determineRobotLaserPath(Player player){
		int xC = player.getRobot().getPosition().getX();
		int yC = player.getRobot().getPosition().getY();
		Orientation orientation = player.getDirection();

		outerLoop1:
		switch(orientation){
			case DOWN: logger.info("Orientation of RobotLaser is DOWN at Coordinate" + "("+ xC + ","+ yC+")" );
				for (int x = xC; x < 10; x++) {
					Tile tileAffected = MapFactory.getInstance().getTile(x, yC);
					for(Attribute b: tileAffected.getAttributes()){
						if(b.getType() != "Wall" ){
							roboCoordinates.add(new Coordinate(x, yC));
							logger.info("RobotLaser Affected Coordinate:" + "(x,y) =" + "("+ x + ","+ yC+")");
							break;
						}
						else if (b.getType() == "Wall"){
							if(b.getOrientation() == Orientation.DOWN){
								roboCoordinates.add(new Coordinate(x,yC));
								logger.info("RobotLaser Affected Coordinate:" + "(x,y) =" + "("+ x + ","+ yC+")");
								break outerLoop1;
							}
						}
					}
				}
			case UP: logger.info("Orientation of RobotLaser is UP at Coordinate" + "("+ xC + ","+ yC+")" );
				for (int x = xC; x >= 0; x--) {
					Tile tileAffected = MapFactory.getInstance().getTile(x, yC);
					for(Attribute b: tileAffected.getAttributes()){
						if(b.getType() != "Wall" ){
							roboCoordinates.add(new Coordinate(x, yC));
							logger.info("RobotLaser Affected Coordinate:" + "(x,y) =" + "("+ x + ","+ yC+")");
							break;
						}
						else if (b.getType() == "Wall"){
							if(b.getOrientation() == Orientation.UP){
								roboCoordinates.add(new Coordinate(x,yC));
								logger.info("RobotLaser Affected Coordinate:" + "(x,y) =" + "("+ x + ","+ yC+")");
								break outerLoop1;
							}
						}
					}
				}
			case RIGHT: logger.info("Orientation of RobotLaser is Right at Coordinate" + "("+ xC + ","+ yC+")" );
				for (int y = yC; y < 10; y++) {
					Tile tileAffected = MapFactory.getInstance().getTile(xC, y);
					for(Attribute b: tileAffected.getAttributes()){
						if(b.getType() != "Wall" ){
							roboCoordinates.add(new Coordinate(xC,y));
							logger.info("RobotLaser Affected Coordinate:" + "(x,y) =" + "("+ xC + ","+ y+")");
							break;
						}
						else if (b.getType() == "Wall"){
							if(b.getOrientation() == Orientation.RIGHT){
								roboCoordinates.add(new Coordinate(xC,y));
								logger.info("RobotLaser Affected Coordinate:" + "(x,y) =" + "("+ xC + ","+ y+")");
								break outerLoop1;
							}
						}
					}
				}
			case LEFT: logger.info("Orientation of RobotLaser is LEFT at Coordinate" + "("+ xC + ","+ yC+")" );
				for (int y = yC; y >= 0; y--) {
					Tile tileAffected = MapFactory.getInstance().getTile(xC, y);
					for(Attribute b: tileAffected.getAttributes()){
						if(b.getType() != "Wall" ){
							roboCoordinates.add(new Coordinate(xC, y));
							logger.info("RobotLaser Affected Coordinate:" + "(x,y) =" + "("+ xC + ","+ y+")");
							break;
						}
						else if (b.getType() == "Wall"){
							if(b.getOrientation() == Orientation.LEFT){
								roboCoordinates.add(new Coordinate(xC,y));
								logger.info("RobotLaser Affected Coordinate:" + "(x,y) =" + "("+ xC + ","+ y+")");
								break outerLoop1;
							}
						}
					}
				}
		}
	}
}
