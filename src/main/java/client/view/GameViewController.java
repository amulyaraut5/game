package client.view;

import game.Player;
import game.gameObjects.maps.Map;
import game.gameObjects.robot.Robot;
import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Empty;
import game.gameObjects.tiles.Laser;
import game.gameObjects.tiles.Wall;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Coordinate;
import utilities.JSONProtocol.body.GameStarted;
import utilities.JSONProtocol.body.SetStartingPoint;
import utilities.MapConverter;
import utilities.SoundHandler;
import utilities.Utilities;
import utilities.enums.AttributeType;
import utilities.enums.Orientation;
import utilities.enums.GameState;
import utilities.enums.Rotation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;

/**
 * The GameViewController class controls the GameView and coordinates its inner views
 *
 * @author Simon, Sarah
 */
public class GameViewController extends Controller {
    private static final Logger logger = LogManager.getLogger();

    private final Group[][] fields = new Group[Utilities.MAP_WIDTH][Utilities.MAP_HEIGHT];
    private HashMap<Player, ImageView> robotImageViews = new HashMap<>();
    private Map map;
    ArrayList<Coordinate> path = new ArrayList<>();

    private PlayerMatController playerMatController;
    private ConstructionController constructionController;
    private ProgrammingController programmingController;
    private ActivationController activationController;
    private OthersController othersController;

    private Pane constructionPane;
    private Pane programmingPane;
    private Pane activationPane;

    private SoundHandler soundHandler;
    private EventHandler<MouseEvent> onMapClicked;

    private GameState currentPhase = GameState.CONSTRUCTION;

    @FXML
    private StackPane playerMap;

    @FXML
    public HBox otherPlayerSpace;
    @FXML
    private BorderPane phasePane;
    @FXML
    private BorderPane chatPane;

    @FXML
    private StackPane boardPane; //stacks the map-, animation-, and playerPane
    @FXML
    private FlowPane mapPane;
    @FXML
    private Pane animationPane;
    @FXML
    private Pane robotPane;

    @FXML
    public void initialize() {
        constructPhaseViews();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/innerViews/playerMat.fxml"));
            playerMap.setAlignment(Pos.CENTER);
            playerMap.getChildren().add(fxmlLoader.load());

            playerMatController = fxmlLoader.getController();
        } catch (IOException e) {
            logger.error("PlayerMap could not be created: " + e.getMessage());
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/innerViews/otherPlayer.fxml"));
            otherPlayerSpace.setSpacing(20);
            otherPlayerSpace.getChildren().add(fxmlLoader.load());
            othersController = fxmlLoader.getController();
        } catch (IOException e) {
            logger.error("Other player maps could not be created: " + e.getMessage());
        }

        boardPane.addEventHandler(MOUSE_CLICKED, onMapClicked = mouseEvent -> {
            int x = (int) mouseEvent.getX() / Utilities.FIELD_SIZE;
            int y = (int) mouseEvent.getY() / Utilities.FIELD_SIZE;
            int pos = x + y * Utilities.MAP_WIDTH;
            client.sendMessage(new SetStartingPoint(pos));
        });

        this.soundHandler = new SoundHandler();
    }

    public void attachChatPane(Pane chat) {
        chat.setPrefWidth(chatPane.getPrefWidth());
        chat.setPrefHeight(chatPane.getPrefHeight());
        chatPane.setCenter(chat);
    }

    public void buildMap(GameStarted gameStarted) {
        map = MapConverter.reconvert(gameStarted);
        int xMax = Utilities.MAP_WIDTH;
        int yMax = Utilities.MAP_HEIGHT;

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

    public void placeRobotInMap(Player player, Coordinate coordinate) {
        if (player.getID() == client.getThisPlayersID()) {
            boardPane.removeEventHandler(MOUSE_CLICKED, onMapClicked);
        }
        player.getRobot().setCoordinate(coordinate);

        ImageView imageView = player.getRobot().drawRobotImage();

        imageView.fitWidthProperty().bind(boardPane.widthProperty().divide(Utilities.MAP_WIDTH));
        imageView.fitHeightProperty().bind(boardPane.heightProperty().divide(Utilities.MAP_HEIGHT));
        imageView.setPreserveRatio(true);

        robotPane.getChildren().add(imageView);
        imageView.setX(coordinate.getX() * Utilities.FIELD_SIZE);
        imageView.setY(coordinate.getY() * Utilities.FIELD_SIZE);

        // Stores the imageView of to change its coordinates later.
        robotImageViews.put(player, imageView);
    }

    public void handleMovement(Player player, Coordinate newPos) {
        ImageView imageView = robotImageViews.get(player);
        Coordinate oldPos = player.getRobot().getCoordinate();

        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(1));
        transition.setNode(imageView);
        transition.setToX((newPos.getX() - oldPos.getX()) * Utilities.FIELD_SIZE);
        transition.setToY((newPos.getY() - oldPos.getY()) * Utilities.FIELD_SIZE);
        transition.play();

        player.getRobot().setCoordinate(newPos);
    }

    public void handlePlayerTurning(Player player, Rotation rotation) {
        ImageView imageView = robotImageViews.get(player);
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
        transition.play();
    }
    public void handleShooting(ArrayList<Player> players){
        for(Coordinate c : map.readLaserCoordinates()) {
            for (Attribute a : map.getTile(c).getAttributes()) {
                ImageView imageView = new ImageView(new Image(getClass().getResource("/tiles/laser/animation/laserBeam_1.png").toExternalForm()));
                imageView.fitWidthProperty().bind(boardPane.widthProperty().divide(Utilities.MAP_WIDTH));
                imageView.fitHeightProperty().bind(boardPane.heightProperty().divide(Utilities.MAP_HEIGHT));
                imageView.setPreserveRatio(true);
                robotPane.getChildren().add(imageView);

                if (a.getType() == AttributeType.Laser) {
                    Orientation orientation = ((Laser) a).getOrientation();
                    switch (orientation) {
                        case LEFT , RIGHT -> imageView.setRotate(270);
                    }

                    Coordinate newPos = calculateEndCoordinate(orientation,c ,players);

                    imageView.setX(c.getX() * Utilities.FIELD_SIZE);
                    imageView.setY(c.getY() * Utilities.FIELD_SIZE);

                    TranslateTransition transition = new TranslateTransition();
                    transition.setDuration(Duration.seconds(2));
                    transition.setNode(imageView);
                    transition.setToX((newPos.getX() - c.getX()) * Utilities.FIELD_SIZE);
                    transition.setToY((newPos.getY() - c.getY()) * Utilities.FIELD_SIZE);

                    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), imageView);
                    fadeTransition.setFromValue(1.0f);
                    fadeTransition.setToValue(0.3f);
                    fadeTransition.setOnFinished(e -> robotPane.getChildren().remove(imageView));

                    SequentialTransition sequentialTransition = new SequentialTransition();
                    sequentialTransition.getChildren().addAll(transition, fadeTransition);
                    sequentialTransition.play();
                    path.clear();
                }
            }
        }
    }
    private Coordinate calculateEndCoordinate(Orientation orientation, Coordinate position,ArrayList<Player> players) {

        path.add(position);
        position = position.clone();
        Coordinate step = orientation.toVector();

        outerLoop:
        while ((position.getX() >= 0 && position.getX() < 13) && (position.getY() >= 0 && position.getY() < 10)) {
            position.add(step);
            for (Attribute b : map.getTile(position.getX(), position.getY()).getAttributes()) {
                if (b.getType() != AttributeType.Wall && b.getType() != AttributeType.Antenna && b.getType() != AttributeType.Laser
                        && b.getType() != AttributeType.ControlPoint) {
                    path.add(position.clone());
                    break;
                } else if (b.getType() == AttributeType.Wall) {
                    if (((Wall) b).getOrientation() == orientation) {
                        path.add(position.clone());
                        break outerLoop;
                    }else if (((Wall) b).getOrientation() != orientation){
                        path.add(position.clone());
                        break outerLoop;
                    }
                }else if (b.getType() == AttributeType.Antenna) break outerLoop;
                else if(b.getType() == AttributeType.Laser){
                    if (((Laser) b).getOrientation() == orientation) {
                        path.add(position.clone());
                        break outerLoop;
                    }else if (((Laser) b).getOrientation() != orientation){
                        path.add(position.clone());
                        break outerLoop;
                    }
                }else if (b.getType() == AttributeType.ControlPoint) {
                    path.add(position.clone());break outerLoop;
                }
            }
        }

        for (Coordinate coordinate: path){
            for(Player player:players){
                if(coordinate.equals(player.getRobot().getCoordinate())) return coordinate;
            }
        }
        return path.get(path.size()-1);
    }

    public ProgrammingController getProgrammingController() {
        return programmingController;
    }

    public ActivationController getActivationController() {
        return activationController;
    }

    public OthersController getOthersController() {
        return othersController;
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
     * Button press to test the change of inner phase panes.
     */
    @FXML
    private void setNextPhaseView() {
        changePhaseView(currentPhase.getNext());
    }

    public void changePhaseView(GameState phase) {
        currentPhase = phase;

        switch (phase) {
            case CONSTRUCTION -> phasePane.setCenter(constructionPane);
            case PROGRAMMING -> {
                phasePane.setCenter(programmingPane);
                othersController.visibleHBoxRegister(true);
            }
            case ACTIVATION -> {
                phasePane.setCenter(activationPane);
                othersController.visibleHBoxRegister(false);
            }
        }
    }

    private void constructPhaseViews() {
        FXMLLoader constructionLoader = new FXMLLoader(getClass().getResource("/view/innerViews/constructionView.fxml"));
        FXMLLoader programmingLoader = new FXMLLoader(getClass().getResource("/view/innerViews/programmingView.fxml"));
        FXMLLoader activationLoader = new FXMLLoader(getClass().getResource("/view/innerViews/activationView.fxml"));

        try {
            constructionPane = constructionLoader.load();
            programmingPane = programmingLoader.load();
            activationPane = activationLoader.load();

            constructionController = constructionLoader.getController();
            programmingController = programmingLoader.getController();
            activationController = activationLoader.getController();
        } catch (IOException e) {
            logger.error("Inner phase View could not be loaded: " + e.getMessage());
        }
    }

    @FXML
    private void soundsOnAction() {
        this.soundHandler.musicOn();
    }

    @FXML
    private void soundsOffAction() {
        this.soundHandler.musicOff();
    }

    public PlayerMatController getPlayerMapController() {
        return playerMatController;
    }

    public void removePlayer(Player player) {
        ImageView imageView = robotImageViews.get(player);
        robotPane.getChildren().remove(imageView);
        //TODO remove small player mat
    }
}