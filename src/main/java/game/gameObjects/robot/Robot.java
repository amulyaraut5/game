package game.gameObjects.robot;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import utilities.Coordinate;
import utilities.ImageHandler;
import utilities.enums.Orientation;
import utilities.enums.Rotation;

/**
 * @author simon
 */
public abstract class Robot {
    protected String imagePath;

    protected String name;
    protected Color color;

    protected Orientation orientation= Orientation.RIGHT;
    protected Coordinate position;
    protected Coordinate oldPosition;

    /**
     * method creates a robot from given figure ID.
     *
     * @param figure figure ID of the wanted robot, from 0 to 5.
     * @return Robot associated with given ID.
     */
    public static Robot create(int figure) {
        Robot robot;

        switch (figure) {
            case 0 -> robot = new Hulk();
            case 1 -> robot = new HammerBot();
            case 2 -> robot = new SmashBot();
            case 3 -> robot = new Twonky();
            case 4 -> robot = new SpinBot();
            case 5 -> robot = new ZoomBot();
            default -> throw new UnsupportedOperationException("Robot ID has to be in between 0 and 5.");
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

        for (int i = 0; i < moveCount; i++) {
            position.add(direction.toVector());
        }

    }

    /**
     * Rotates the robots orientation by 90°.
     *
     * @param rotation Direction to rotate. Either LEFT or RIGHT.
     */
    public void rotate(Rotation rotation) {
        switch (rotation) {
            case RIGHT -> orientation = orientation.getNext();
            case LEFT -> orientation = orientation.getPrevious();
            //default -> throw new UnsupportedOperationException()
        }

    }

    public ImageView drawRobotImage() {
        return ImageHandler.createImageView(imagePath, orientation);

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

    public void setPosition(Coordinate p) {
        oldPosition = position;
        position = p;
    }

    public Coordinate getOldPosition() {
        return oldPosition;
    }

    public void setPosition(int x, int y) {
        oldPosition = position;
        position = new Coordinate(x, y);
    }

    //TODO
    public void reboot() {
    }
}
