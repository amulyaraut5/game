package game.gameObjects.tiles;

import game.gameObjects.Coordinate;
import game.gameObjects.Utilities;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import jdk.jshell.execution.Util;

import java.util.ArrayList;

public class Tile {

	protected String imagePath;
	private Attribute attribute;

	public Tile(Attribute attribute){
		this.attribute = attribute;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	public void draw(GraphicsContext gc, Coordinate position) {
		//TODO define image path
		final Image image = new Image(imagePath);
		final int size = 100;//TODO define size (w,h)
		gc.drawImage(image, size * position.getX(), size * position.getY(), size, size);
	}
	
	public Tile createTile(int tileID){
		Tile tile = null;
		switch(tileID){
			case 0: tile = new Tile(new Antenna());
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