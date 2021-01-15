package game.gameObjects.robot;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import utilities.Coordinate;
import utilities.Orientation;
import utilities.Utilities.Rotation;

/**
 * @author simon
 */
public abstract class Robot {
    protected String imagePath;

    protected String name;
    protected Color color;

    protected Orientation orientation;
    protected Coordinate position;

    public static Robot create(int figure) {
        Robot robot = null;

        switch (figure) {
            case 0 -> robot = new Hulk();
            case 1 -> robot = new HammerBot();
            case 2 -> robot = new SmashBot();
            case 3 -> robot = new Twonky();
            case 4 -> robot = new SpinBot();
            case 5 -> robot = new ZoomBot();
        }
        return robot;
    }

    /**
     * Moves the robot regarding its orientation. It is not checked if the move is allowed.
     * <pre>
     * {@code move(3)} moves the robot 3 fields forward.
     * {@code move(-1)} moves the robot 1 field backwards.
     * </pre>
     *
     * @param moveCount Number of fields which the robot moves.
     */
    public void move(int moveCount) {
        move(moveCount, orientation);
    }

    /**
     * Moves the robot in given direction. It is not checked if the move is allowed.
     * <pre>
     * {@code move(3, UP)} moves the robot 3 fields up (i.e. y-3 ).
     * {@code move(-1, LEFT)} moves the robot 1 field to the right (i.e. x+1 ).
     * </pre>
     *
     * @param moveCount Number of fields which the robot moves.
     * @param direction Direction of the movement
     */
    public void move(int moveCount, Orientation direction) {
        int x = position.getX();
        int y = position.getY();
        //position.add(direction.toVector());

        switch (direction) {
            case UP -> position.setY(y - moveCount);
            case RIGHT -> position.setX(x + moveCount);
            case DOWN -> position.setY(y + moveCount);
            case LEFT -> position.setX(x - moveCount);
        }
    }

    /**
     * Rotates the robots orientation by 90°.
     *
     * @param rotation Direction to rotate. Either LEFT or RIGHT.
     */
    public void rotate(Rotation rotation) {
        switch (rotation) {
            case RIGHT:
                switch (getOrientation()) {
                    case UP -> setOrientation(Orientation.RIGHT);
                    case RIGHT -> setOrientation(Orientation.DOWN);
                    case DOWN -> setOrientation(Orientation.LEFT);
                    case LEFT -> setOrientation(Orientation.UP);
                }
                break;
            case LEFT:
                switch (getOrientation()) {
                    case UP -> setOrientation(Orientation.LEFT);
                    case LEFT -> setOrientation(Orientation.DOWN);
                    case DOWN -> setOrientation(Orientation.RIGHT);
                    case RIGHT -> setOrientation(Orientation.UP);
                }
                break;
        }
    }

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
        this.position = new Coordinate(x, y);
    }
}