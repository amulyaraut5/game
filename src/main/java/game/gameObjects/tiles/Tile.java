package game.gameObjects.tiles;

import utilities.Coordinate;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utilities.Utilities;

import java.util.ArrayList;

public class Tile {

	protected String imagePath;
	private Attribute attribute1;
	private Attribute attribute2;




	/**
	 * Constructor for tiles with one attribute.
	 * @param attribute1 first attribute
	 */
	public Tile(Attribute attribute1){
		this.attribute1 = attribute1;
	}

	/**
	 * Constructor for tiles with two attributes.
	 * @param attribute1 first attribute
	 * @param attribute2 second attribute
	 */
	public Tile (Attribute attribute1,Attribute attribute2){
		this.attribute1 = attribute1;
		this.attribute2 = attribute2;
	}

	public Attribute getAttribute() {
		return attribute1;
	}

	public void setAttribute(Attribute attribute1) {
		this.attribute1 = attribute1;
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

			case 111: tile = new Tile(new Antenna());
					// draw();
				// TODO should be changed
				// Belt constructor has 2 attributes.

			//Green Conveyor Belts
			case 01: tile = new Tile(new Belt(Utilities.Orientation.UP, 1));
			case 02: tile = new Tile(new Belt(Utilities.Orientation.DOWN, 1));
			case 03: tile = new Tile(new Belt(Utilities.Orientation.LEFT, 1));
			case 04: tile = new Tile(new Belt(Utilities.Orientation.RIGHT, 1));
			// draw();

			//Blue Conveyor Belts
			case 11: tile = new Tile(new Belt(Utilities.Orientation.UP, 2));
			case 12: tile = new Tile(new Belt(Utilities.Orientation.DOWN, 2));
			case 13: tile = new Tile(new Belt(Utilities.Orientation.LEFT, 2));
			case 14: tile = new Tile(new Belt(Utilities.Orientation.RIGHT, 2));
				// draw();

			//Green Rotating Conveyor Belts
			case 21: tile = new Tile(new RotatingBelt(Utilities.upLeft, false, 1));
			case 22: tile = new Tile(new RotatingBelt(Utilities.upRight, false, 1));
			case 23: tile = new Tile(new RotatingBelt(Utilities.downLeft, false, 1));
			case 24: tile = new Tile(new RotatingBelt(Utilities.downRight, false, 1));

			case 25: tile = new Tile(new RotatingBelt(Utilities.upLeft, true, 1));
			case 26: tile = new Tile(new RotatingBelt(Utilities.upRight, true, 1));
			case 27: tile = new Tile(new RotatingBelt(Utilities.downLeft, true, 1));
			case 28: tile = new Tile(new RotatingBelt(Utilities.downRight, true, 1));
			// draw();

			//Blue Rotating Conveyor Belts
			case 31: tile = new Tile(new RotatingBelt(Utilities.upLeft, false, 1));
			case 32: tile = new Tile(new RotatingBelt(Utilities.upRight, false, 1));
			case 33: tile = new Tile(new RotatingBelt(Utilities.downLeft, false, 1));
			case 34: tile = new Tile(new RotatingBelt(Utilities.downRight, false, 1));

			case 35: tile = new Tile(new RotatingBelt(Utilities.upLeft, true, 1));
			case 36: tile = new Tile(new RotatingBelt(Utilities.upRight, true, 1));
			case 37: tile = new Tile(new RotatingBelt(Utilities.downLeft, true, 1));
			case 38: tile = new Tile(new RotatingBelt(Utilities.downRight, true, 1));
			// draw();

			//Push Panels
			case 40: tile = new Tile(new PushPanel(Utilities.Orientation.DOWN, new int[]{2, 4}));
			case 41: tile = new Tile(new PushPanel(Utilities.Orientation.UP, new int[]{2,4}));
			case 42: tile = new Tile(new PushPanel(Utilities.Orientation.LEFT ,new int[]{2,4}));
			case 43: tile = new Tile(new PushPanel(Utilities.Orientation.RIGHT, new int[]{2,4}));

			case 44: tile = new Tile(new PushPanel(Utilities.Orientation.DOWN, new int[]{1, 3, 5}));
			case 45: tile = new Tile(new PushPanel(Utilities.Orientation.UP, new int[]{1, 3, 5}));
			case 46: tile = new Tile(new PushPanel(Utilities.Orientation.LEFT, new int[]{1, 3, 5}));
			case 47: tile = new Tile(new PushPanel(Utilities.Orientation.RIGHT, new int[]{1, 3, 5}));

			//Gears
			case 51: tile = new Tile(new Gear(Utilities.Orientation.LEFT));
			case 52: tile = new Tile(new Gear(Utilities.Orientation.RIGHT));
			// draw();

			//Board Lasers
			case 61: tile = new Tile(new Laser(Utilities.Orientation.DOWN, 1));
			case 62: tile = new Tile(new Laser(Utilities.Orientation.UP,1));
			case 63: tile = new Tile(new Laser(Utilities.Orientation.LEFT,1));
			case 64: tile = new Tile(new Laser(Utilities.Orientation.RIGHT,1));

			case 65: tile = new Tile(new Laser(Utilities.Orientation.DOWN, 2));
			case 66: tile = new Tile(new Laser(Utilities.Orientation.UP,2));
			case 67: tile = new Tile(new Laser(Utilities.Orientation.LEFT,2));
			case 68: tile = new Tile(new Laser(Utilities.Orientation.RIGHT,2));

			case 71: tile = new Tile(new Laser(Utilities.Orientation.DOWN, 3));
			case 72: tile = new Tile(new Laser(Utilities.Orientation.UP,3));
			case 73: tile = new Tile(new Laser(Utilities.Orientation.LEFT,3));
			case 74: tile = new Tile(new Laser(Utilities.Orientation.RIGHT,3));
			// draw();

			//Energy Spaces
			case 81: tile = new Tile(new EnergySpace(0));
			case 82: tile = new Tile(new EnergySpace(1));
			// draw();

			//Pits
			case 90: tile = new Tile(new Pit());

			//Walls
			case 91: tile = new Tile(new Wall(Utilities.Orientation.DOWN));
			case 92: tile = new Tile(new Wall(Utilities.Orientation.UP));
			case 93: tile = new Tile(new Wall(Utilities.Orientation.LEFT));
			case 94: tile = new Tile(new Wall(Utilities.Orientation.RIGHT));

			// Tile with two walls

		}
		return tile;
	}
}