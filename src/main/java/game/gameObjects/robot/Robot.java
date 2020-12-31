package game.gameObjects.robot;

import game.Player;
import utilities.Coordinate;
import utilities.Utilities.Orientation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * @author simon
 */
public abstract class Robot {

    protected String name;
    protected Color color;
    protected String imagePath;

    /**
     * direction in which the robot is facing
     */
    protected Orientation orientation;
    /**
     * position of the robot on the map
     */
    protected Coordinate position;

    public void draw(GraphicsContext gc) {
        final Image image = new Image(imagePath);
        final int size = 100;//TODO define size (w,h)
        gc.drawImage(image, size * position.getX(), size * position.getY(), size, size);
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }


    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }
    public void setPosition(int x, int y) {
        this.position = new Coordinate(x,y);
    }


}