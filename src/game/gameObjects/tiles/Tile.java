package game.gameObjects.tiles;

import game.gameObjects.Coordinate;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class Tile {

	protected boolean wallNorth;
	protected boolean wallEast;
	protected boolean wallSouth;
	protected boolean wallWest;
	protected String imagePath;
	private ArrayList<Attribute> attributes;

	public void draw(GraphicsContext gc, Coordinate position) {
		//TODO define image path
		final Image image = new Image(imagePath);
		final int size = 100;//TODO define size (w,h)
		gc.drawImage(image, size * position.getX(), size * position.getY(), size, size);
	}
}