package client.view;

import game.Player;
import game.gameObjects.maps.Map;
import game.gameObjects.robot.Robot;
import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Empty;
import game.gameObjects.tiles.Laser;
import game.round.LaserAction;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import utilities.Coordinate;
import utilities.JSONProtocol.body.GameStarted;
import utilities.JSONProtocol.body.SendChat;
import utilities.JSONProtocol.body.SetStartingPoint;
import utilities.MapConverter;
import utilities.Constants;
import utilities.enums.AttributeType;
import utilities.enums.Orientation;
import utilities.enums.Rotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static javafx.scene.input.MouseEvent.*;

public class GameBoardController extends Controller {
    private final Group[][] fields = new Group[Constants.MAP_WIDTH][Constants.MAP_HEIGHT];
    private final HashMap<Player, ImageView> robotTokens = new HashMap<>();
    private Map map;
    private boolean isStartPosSet = false;

    @FXML
    private StackPane boardPane; //stacks the map-, animation-, and playerPane
    @FXML
    private FlowPane mapPane;
    @FXML
    private Pane animationPane;
    @FXML
    private Pane robotPane;
    @FXML
    private Label labelPosition;
    @FXML
    private Label labelCoordinate;

    @FXML
    private void initialize() {
        boardPane.addEventHandler(MOUSE_CLICKED, this::onMapClicked);
        boardPane.addEventHandler(MOUSE_EXITED, mouseEvent -> {
            labelPosition.setText("");
            labelCoordinate.setText("");
        });
        boardPane.addEventHandler(MOUSE_MOVED, mouseEvent -> {
            int x = (int) mouseEvent.getX() / Constants.FIELD_SIZE;
            int y = (int) mouseEvent.getY() / Constants.FIELD_SIZE;
            int position = new Coordinate(x, y).toPosition();
            labelPosition.setText(position + ":");
            labelCoordinate.setText(new Coordinate(x, y).toString());
        });
    }

    private void onMapClicked(MouseEvent mouseEvent) {
        int x = (int) mouseEvent.getX() / Constants.FIELD_SIZE;
        int y = (int) mouseEvent.getY() / Constants.FIELD_SIZE;
        int position = new Coordinate(x, y).toPosition();

        if (!isStartPosSet) {
            client.sendMessage(new SetStartingPoint(position));
        } else {
            client.sendMessage(new SendChat("#tp " + position, -1));
        }
    }

    public void buildMap(GameStarted gameStarted) {
        map = MapConverter.reconvert(gameStarted);
        int xMax = Constants.MAP_WIDTH;
        int yMax = Constants.MAP_HEIGHT;

        for (int y = 0; y < yMax; y++) {
            for (int x = 0; x < xMax; x++) {
                ArrayList<Attribute> attributes = map.getTile(x, y).getAttributes();
                fields[x][y] = new Group();

                if (isTileBackgroundEmpty(attributes)) {    //If the image would be transparent, an empty tile is added.
                    fields[x][y].getChildren().add(new Empty().createImage());
                }

                attributes = sortBoardAttributes(attributes);
                for (Attribute attribute : attributes) {
                    Node attributeImage = attribute.createImage();
                    if (attributeImage != null) {
                        fields[x][y].getChildren().add(attributeImage);
                    }
                }
                mapPane.getChildren().add(fields[x][y]);
            }
        }
    }

    private ArrayList<Attribute> sortBoardAttributes(ArrayList<Attribute> field) {
        var sortedField = new ArrayList<Attribute>();
        AttributeType[][] priorityArray = {
                {AttributeType.Pit, AttributeType.Empty},
                {AttributeType.Belt, AttributeType.RotatingBelt, AttributeType.Gear,
                        AttributeType.EnergySpace, AttributeType.Antenna},
                {AttributeType.ControlPoint, AttributeType.RestartPoint, AttributeType.StartPoint},
                {AttributeType.PushPanel, AttributeType.Laser},
                {AttributeType.Wall}};

        for (AttributeType[] priority : priorityArray) {
            List<AttributeType> priorityList = Arrays.asList(priority);
            for (Attribute a : field) {
                AttributeType type = a.getType();
                if (priorityList.contains(type)) {
                    sortedField.add(a);
                }
            }
        }
        return sortedField;
    }

    /**
     * Method tests if the background of all attributes on a field are transparent.
     *
     * @param field Field to test
     * @return True if the Tile is transparent.
     */
    private boolean isTileBackgroundEmpty(ArrayList<Attribute> field) {
        for (Attribute a : field) {
            if (a.getType() == AttributeType.Empty || a.getType() == AttributeType.Pit) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method places the robot on the board based on the starting point player chooses.
     *
     * @param player
     * @param coordinate
     */
    public void placeRobotInMap(Player player, Coordinate coordinate) {
        if (player.getID() == client.getThisPlayersID()) {
            isStartPosSet = true;//boardPane.removeEventHandler(MOUSE_CLICKED, onMapClicked);
        }
        player.getRobot().setCoordinate(coordinate);
        ImageView imageView = player.getRobot().drawRobotImage();

        imageView.fitWidthProperty().bind(boardPane.widthProperty().divide(Constants.MAP_WIDTH));
        imageView.fitHeightProperty().bind(boardPane.heightProperty().divide(Constants.MAP_HEIGHT));
        imageView.setPreserveRatio(true);

        robotPane.getChildren().add(imageView);
        imageView.setX(coordinate.getX() * Constants.FIELD_SIZE);
        imageView.setY(coordinate.getY() * Constants.FIELD_SIZE);

        // Stores the imageView of to change its coordinates later.
        robotTokens.put(player, imageView);
    }

    /**
     * This method moves the player in the view based on the argument received
     * and updates the player position after every method call on the client side to keep track of position
     * to update the view later.
     *
     * @param player Player whose robot should be moved
     * @param newPos new Position of robot after movement is handled
     */
    public void handleMovement(Player player, Coordinate newPos) {
        ImageView imageView = robotTokens.get(player);
        Coordinate oldPos = player.getRobot().getCoordinate();
        player.getRobot().setCoordinate(newPos);

        imageView.setX(oldPos.getX() * Constants.FIELD_SIZE);
        imageView.setY(oldPos.getY() * Constants.FIELD_SIZE);

        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(1));
        transition.setNode(imageView);
        transition.setToX((newPos.getX() - oldPos.getX()) * Constants.FIELD_SIZE);
        transition.setToY((newPos.getY() - oldPos.getY()) * Constants.FIELD_SIZE);
        transition.setOnFinished(event -> {
            imageView.setX((oldPos.getX() * Constants.FIELD_SIZE) + imageView.getTranslateX());
            imageView.setY((oldPos.getY() * Constants.FIELD_SIZE) + imageView.getTranslateY());
            imageView.setTranslateX(0);
            imageView.setTranslateY(0);
        });
        transition.play();
    }

    /**
     * This method rotates the player in the view based on the argument received and updates
     * the player orientation after every method call on the client side to keep track of
     * orientation to update the view later.
     *
     * @param player   Player whose robot should be turned
     * @param rotation Parameter that determines how the player should be rotated.
     */

    public void handlePlayerTurning(Player player, Rotation rotation) {
        ImageView imageView = robotTokens.get(player);
        int angle = 0;
        Robot r = player.getRobot();
        if (rotation == Rotation.LEFT) {
            angle = -90;
            r.setOrientation(r.getOrientation().getPrevious());
        } else if (rotation == Rotation.RIGHT) {
            angle = 90;
            r.setOrientation(r.getOrientation().getNext());
        }

        RotateTransition transition = new RotateTransition();
        transition.setDuration(Duration.seconds(1));
        transition.setNode(imageView);
        transition.setByAngle(angle);

        transition.setOnFinished(event -> {
            switch (r.getOrientation()) {
                case UP -> imageView.setRotate(0);
                case DOWN -> imageView.setRotate(180);
                case RIGHT -> imageView.setRotate(90);
                case LEFT -> imageView.setRotate(270);
            }
        });

        transition.play();
    }

    /**
     * This method only shows the animation of laser beam being fired in the view.
     * The laser beam will terminate abruptly if it hits the robot or wall on it's way.
     *
     * @param players active player list
     */

    public void handleShooting(ArrayList<Player> players) {
        for (Coordinate c : map.readLaserCoordinates()) {
            for (Attribute a : map.getTile(c).getAttributes()) {
                ImageView imageView = new ImageView(new Image(getClass().getResource("/tiles/laser/animation/laserBeam_1.png").toExternalForm()));
                imageView.fitWidthProperty().bind(boardPane.widthProperty().divide(Constants.MAP_WIDTH));
                imageView.fitHeightProperty().bind(boardPane.heightProperty().divide(Constants.MAP_HEIGHT));
                imageView.setPreserveRatio(true);

                if (a.getType() == AttributeType.Laser) {
                    Orientation orientation = ((Laser) a).getOrientation();
                    switch (orientation) {
                        case LEFT, RIGHT -> imageView.setRotate(270);
                    }

                    animationPane.getChildren().add(imageView);
                    Coordinate newPos = LaserAction.calculateLaserEnd(c, orientation, map, players);
                    imageView.setX(c.getX() * Constants.FIELD_SIZE);
                    imageView.setY(c.getY() * Constants.FIELD_SIZE);

                    TranslateTransition transition = new TranslateTransition();
                    transition.setDuration(Duration.seconds(2));
                    transition.setNode(imageView);
                    transition.setToX((newPos.getX() - c.getX()) * Constants.FIELD_SIZE);
                    transition.setToY((newPos.getY() - c.getY()) * Constants.FIELD_SIZE);
                    transition.setOnFinished(e -> animationPane.getChildren().remove(imageView));

                    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), imageView);
                    fadeTransition.setFromValue(1.0f);
                    fadeTransition.setToValue(0.0f);
                    fadeTransition.setOnFinished(e -> animationPane.getChildren().remove(imageView));

                    SequentialTransition sequentialTransition = new SequentialTransition();
                    sequentialTransition.getChildren().addAll(transition, fadeTransition);
                    sequentialTransition.play();
                }
            }
        }
    }

    /**
     * This method only shows the animation of laser beam being fired in the view.
     * The laser beam will terminate abruptly if it hits the robot or wall on it's way.
     * The robot can fire from any position as long as it is playing in the current round.
     *
     * @param players active player list
     */

    public void handleRobotShooting(ArrayList<Player> players) {
        for (Player player : players) {
            Orientation orientation = player.getRobot().getOrientation();
            Coordinate robotPosition = player.getRobot().getCoordinate();

            ImageView imageView = new ImageView(new Image(getClass().getResource("/tiles/laser/animation/laserBeam_1.png").toExternalForm()));
            imageView.fitWidthProperty().bind(boardPane.widthProperty().divide(Constants.MAP_WIDTH));
            imageView.fitHeightProperty().bind(boardPane.heightProperty().divide(Constants.MAP_HEIGHT));
            imageView.setPreserveRatio(true);
            animationPane.getChildren().add(imageView);

            switch (orientation) {
                case LEFT, RIGHT -> imageView.setRotate(270);
            }

            Coordinate newPos = LaserAction.calculateLaserEnd(robotPosition, orientation, map, players);
            imageView.setX(robotPosition.getX() * Constants.FIELD_SIZE);
            imageView.setY(robotPosition.getY() * Constants.FIELD_SIZE);

            TranslateTransition transition = new TranslateTransition();
            transition.setDuration(Duration.seconds(2));
            transition.setNode(imageView);
            transition.setToX((newPos.getX() - robotPosition.getX()) * Constants.FIELD_SIZE);
            transition.setToY((newPos.getY() - robotPosition.getY()) * Constants.FIELD_SIZE);
            transition.setOnFinished(e -> animationPane.getChildren().remove(imageView));

            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), imageView);
            fadeTransition.setFromValue(1.0f);
            fadeTransition.setToValue(0.3f);
            fadeTransition.setOnFinished(e -> animationPane.getChildren().remove(imageView));

            SequentialTransition sequentialTransition = new SequentialTransition();
            sequentialTransition.getChildren().addAll(transition, fadeTransition);
            sequentialTransition.play();
        }
    }

    public void removePlayer(Player player) {
        ImageView imageView = robotTokens.get(player);
        robotPane.getChildren().remove(imageView);
    }
}