package game.gameObjects.tiles;

import utilities.Coordinate;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utilities.Utilities;

import java.util.ArrayList;

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
		ArrayList<Utilities.Orientation> orientations;
		switch(tileID){
			case 00: tile = new Tile(new Empty());

			case 01: tile = new Tile(new Antenna());
					// draw();
				// TODO should be changed
				// Belt constructor has 2 attributes.

			case 11: tile = new Tile(new Belt(Utilities.upLeft, 1));
			case 12: tile = new Tile(new Belt(Utilities.upRight, 1));
			case 13: tile = new Tile(new Belt(Utilities.downLeft, 1));
			case 14: tile = new Tile(new Belt(Utilities.downRight, 1));
			case 15: tile = new Tile(new Belt(Utilities.Orientation.UP, 1));
			case 16: tile = new Tile(new Belt(Utilities.Orientation.DOWN, 1));
			case 17: tile = new Tile(new Belt(Utilities.Orientation.LEFT, 1));
			case 18: tile = new Tile(new Belt(Utilities.Orientation.RIGHT, 1));


			// draw();
			case 21: tile = new Tile(new RotatingBelt(Utilities.upLeft, false));
			case 22: tile = new Tile(new RotatingBelt(Utilities.upRight, false));
			case 23: tile = new Tile(new RotatingBelt(Utilities.downLeft, false));
			case 24: tile = new Tile(new RotatingBelt(Utilities.downRight, false));
			// draw();
			case 31: tile = new Tile(new Laser(Utilities.Orientation.DOWN, 1));
			case 32: tile = new Tile(new Laser(Utilities.Orientation.UP,1));
			case 33: tile = new Tile(new Laser(Utilities.Orientation.LEFT,1));
			case 34: tile = new Tile(new Laser(Utilities.Orientation.RIGHT,1));
			// draw();
			case 41: tile = new Tile(new EnergySpace(1));
			// draw();
			case 51: tile = new Tile(new Pit());

			case 61: tile = new Tile(new Gear(Utilities.Orientation.LEFT));
			case 62: tile = new Tile(new Gear(Utilities.Orientation.RIGHT));
			// draw();
			case 711: tile = new Tile(new PushPanel(Utilities.Orientation.DOWN,1));
			case 712: tile = new Tile(new PushPanel(Utilities.Orientation.UP,1));
			case 713: tile = new Tile(new PushPanel(Utilities.Orientation.LEFT,1));
			case 714: tile = new Tile(new PushPanel(Utilities.Orientation.RIGHT,1));

			case 721: tile = new Tile(new PushPanel(Utilities.Orientation.DOWN,2));
			case 722: tile = new Tile(new PushPanel(Utilities.Orientation.UP,2));
			case 723: tile = new Tile(new PushPanel(Utilities.Orientation.LEFT,2));
			case 724: tile = new Tile(new PushPanel(Utilities.Orientation.RIGHT,2));

			case 731: tile = new Tile(new PushPanel(Utilities.Orientation.DOWN,3));
			case 732: tile = new Tile(new PushPanel(Utilities.Orientation.UP,3));
			case 733: tile = new Tile(new PushPanel(Utilities.Orientation.LEFT,3));
			case 734: tile = new Tile(new PushPanel(Utilities.Orientation.RIGHT,3));

			case 81: tile = new Tile(new Wall(Utilities.Orientation.DOWN));
			case 82: tile = new Tile(new Wall(Utilities.Orientation.UP));
			case 83: tile = new Tile(new Wall(Utilities.Orientation.LEFT));
			case 84: tile = new Tile(new Wall(Utilities.Orientation.RIGHT));

			// Tile with two walls

		}
		return tile;
	}
}