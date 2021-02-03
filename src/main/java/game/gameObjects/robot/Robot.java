package game.gameObjects.robot;

import game.Player;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import server.Server;
import utilities.Coordinate;
import utilities.ImageHandler;
import utilities.JSONProtocol.body.Movement;
import utilities.JSONProtocol.body.PlayerTurning;
import utilities.enums.Orientation;
import utilities.enums.Rotation;

/**
 * @author simon
 */
public abstract class Robot {
    private final Server server = Server.getInstance();
    private final Player player;

    protected final String imagePath;
    protected final String name;
    protected final Color color;

    protected Orientation orientation = Orientation.RIGHT;
    protected Coordinate coordinate;
    private Coordinate startingPoint;

    protected Robot(Player player, String imagePath, String name, Color color) {
        this.player = player;
        this.imagePath = imagePath;
        this.name = name;
        this.color = color;
    }

    /**
     * method creates a robot from given figure ID.
     *
     * @param figure figure ID of the wanted robot, from 0 to 5.
     * @return Robot associated with given ID.
     */
    public static Robot create(int figure, Player player) {
        Robot robot;

        switch (figure) {
            case 0 -> robot = new Hulk(player);
            case 1 -> robot = new HammerBot(player);
            case 2 -> robot = new SmashBot(player);
            case 3 -> robot = new Twonky(player);
            case 4 -> robot = new SpinBot(player);
            case 5 -> robot = new ZoomBot(player);
            default -> throw new UnsupportedOperationException("Robot ID has to be in between 0 and 5.");
        }
        return robot;
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
        for (int i = 0; i < moveCount; i++) {
            coordinate.add(direction.toVector());
        }
        //server.communicateAll(new Movement(player.getID(), coordinate.toPosition()));
    }

    public void moveTo(Coordinate pos) {
        coordinate = pos.clone();
        server.communicateAll(new Movement(player.getID(), coordinate.toPosition()));
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
        }
    }

    public void rotateTo(Orientation rotateTo) {
        if (rotateTo == orientation.getOpposite()) {
            server.communicateAll(new PlayerTurning(player.getID(), Rotation.RIGHT));
            server.communicateAll(new PlayerTurning(player.getID(), Rotation.RIGHT));
        } else if (rotateTo == orientation.getNext()) {
            server.communicateAll(new PlayerTurning(player.getID(), Rotation.RIGHT));
        } else if (rotateTo == orientation.getPrevious()) {
            server.communicateAll(new PlayerTurning(player.getID(), Rotation.LEFT));
        }
        orientation = rotateTo;
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

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate p) {
        coordinate = p;
    }

    public Coordinate getStartingPoint(){
        return this.startingPoint;
    }

    public void setStartingPoint(Coordinate coordinate){
        this.startingPoint = coordinate;
    }
}
