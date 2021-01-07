package game.round;

import game.Player;
import game.gameObjects.maps.MapFactory;
import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Tile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Coordinate;
import utilities.Utilities.Orientation;

import java.util.ArrayList;

/**
 * This class handles the functionality of boardLasers as well as RobotLasers.
 * @author Amulya
 */

public class Laser {

	ArrayList<Coordinate> coordinates = new ArrayList<>();
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
					 //player.getDiscardedProgrammingDeck().addCard();

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
					outerLoop:
					if (a.getOrientation() == Orientation.RIGHT) {
						logger.info("Orientation of Laser is Right at Coordinate" + "("+ xC + ","+ yC+")" );
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
					}

					else if (a.getOrientation() == Orientation.LEFT) {
						logger.info("Orientation of Laser is LEFT at Coordinate" + "("+ xC + ","+ yC+")" );
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
					} else if (a.getOrientation() == Orientation.DOWN) {
						logger.info("Orientation of Laser is DOWN at Coordinate" + "("+ xC + ","+ yC+")" );
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
					} else if (a.getOrientation() == Orientation.UP) {
						logger.info("Orientation of Laser is UP at Coordinate" + "("+ xC + ","+ yC+")" );
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
	 * The lasers will only activate if any robot is standing in the sight of another robot.
	 * @return
	 */

	public void activateRobotLaser(Player player){
		int x = player.getRobot().getPosition().getX();
		int y = player.getRobot().getPosition().getY();
		Orientation orientation = player.getDirection();

	}
}