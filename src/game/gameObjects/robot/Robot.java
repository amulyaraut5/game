package game.gameObjects.robot;

import game.gameObjects.Coordinate;
import game.gameObjects.Utilities.Direction;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author simon
 */
public abstract class Robot {

    protected String name;
    protected Color color;
    protected String imageLocation;

    /**
     * direction in which the robot is facing
     */
    protected Direction direction;
    /**
     * position of the robot on the map
     */
    protected Coordinate position;

    protected void draw(GraphicsContext gc) {
        final Image image = new Image(imageLocation);
        //TODO set x and y (each with the multiplier of the size) define size (w,h)
        gc.drawImage(image, x, y, w, h);
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }
}