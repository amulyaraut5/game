package game.gameObjects.robot;

import game.gameObjects.Coordinate;
import game.gameObjects.Utilities.Direction;
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
    protected Direction direction;
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

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Coordinate getPosition() {
        return position;
    }

    //changed -->
    public Coordinate setPosition(int x, int y) {
        return new Coordinate(x,y);
    }
}