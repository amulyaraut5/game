package game.gameObjects.tiles;

import game.gameObjects.Coordinate;
import Utilities.Utilities;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Tile {

	protected String imagePath;
	private Attribute attribute;
	private Attribute attribute2;



	/**
	 * Constructor for tiles with one attribute.
	 * @param attribute first attribute
	 */
	public Tile(Attribute attribute){
		this.attribute = attribute;
	}

	/**
	 * Cnstructor for tiles with two attributes.
	 * @param attribute1 first attribute
	 * @param attribute2 second attribute
	 */
	public Tile (Attribute attribute1,Attribute attribute2){
		this.attribute = attribute1;
		this.attribute2 = attribute2;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	/**
	 * It sets the priority order of execution of different attributes of tiles.
	 */
	public void priorityOrder(){
		//TODO
	}

	public void draw(GraphicsContext gc, Coordinate position) {
		//TODO define image path
		final Image image = new Image(imagePath);
		final int size = 100;//TODO define size (w,h)
		gc.drawImage(image, size * position.getX(), size * position.getY(), size, size);
	}

	/**
	 * This method creates the tile with specific attribute.
	 * Every tile has then it's own specific id which can be called while laying out the map structure.
	 * Depending upon the needs of map, we can create our own tile with multiple attributes with different orientations.
	 * @param tileID
	 * @return tile
	 */
	public Tile createTile(int tileID){
		Tile tile = null;
		switch(tileID){
			case 00: tile = new Tile(new EmptyAttribute());

			case 01: tile = new Tile(new Antenna());
					// draw();
			case 11: tile = new Tile(new BlueConveyor(Utilities.Direction.SOUTH));
			case 12: tile = new Tile(new BlueConveyor(Utilities.Direction.NORTH));
			case 13: tile = new Tile(new BlueConveyor(Utilities.Direction.EAST));
			case 14: tile = new Tile(new BlueConveyor(Utilities.Direction.WEST));
			// draw();
			case 21: tile = new Tile(new GreenConveyor(Utilities.Direction.SOUTH));
			case 22: tile = new Tile(new GreenConveyor(Utilities.Direction.NORTH));
			case 23: tile = new Tile(new GreenConveyor(Utilities.Direction.EAST));
			case 24: tile = new Tile(new GreenConveyor(Utilities.Direction.WEST));
			// draw();
			case 31: tile = new Tile(new BoardLaser(Utilities.Direction.SOUTH));
			case 32: tile = new Tile(new BoardLaser(Utilities.Direction.NORTH));
			case 33: tile = new Tile(new BoardLaser(Utilities.Direction.EAST));
			case 34: tile = new Tile(new BoardLaser(Utilities.Direction.WEST));
			// draw();
			case 41: tile = new Tile(new EnergySpace());
			// draw();
			case 51: tile = new Tile(new Pit());

			case 61: tile = new Tile(new Gear(Utilities.Rotation.LEFT));
			case 62: tile = new Tile(new Gear(Utilities.Rotation.RIGHT));
			// draw();
			case 711: tile = new Tile(new PushPanel(Utilities.Direction.SOUTH,1));
			case 712: tile = new Tile(new PushPanel(Utilities.Direction.NORTH,1));
			case 713: tile = new Tile(new PushPanel(Utilities.Direction.EAST,1));
			case 714: tile = new Tile(new PushPanel(Utilities.Direction.WEST,1));

			case 721: tile = new Tile(new PushPanel(Utilities.Direction.SOUTH,2));
			case 722: tile = new Tile(new PushPanel(Utilities.Direction.NORTH,2));
			case 723: tile = new Tile(new PushPanel(Utilities.Direction.EAST,2));
			case 724: tile = new Tile(new PushPanel(Utilities.Direction.WEST,2));

			case 731: tile = new Tile(new PushPanel(Utilities.Direction.SOUTH,3));
			case 732: tile = new Tile(new PushPanel(Utilities.Direction.NORTH,3));
			case 733: tile = new Tile(new PushPanel(Utilities.Direction.EAST,3));
			case 734: tile = new Tile(new PushPanel(Utilities.Direction.WEST,3));

			case 81: tile = new Tile(new Wall(Utilities.Direction.SOUTH));
			case 82: tile = new Tile(new Wall(Utilities.Direction.NORTH));
			case 83: tile = new Tile(new Wall(Utilities.Direction.EAST));
			case 84: tile = new Tile(new Wall(Utilities.Direction.WEST));

			// Tile with two walls

		}
		return tile;
	}
}