package client.view;

import game.Player;
import game.gameObjects.maps.Map;
import game.gameObjects.robot.Robot;
import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Empty;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Coordinate;
import utilities.JSONProtocol.body.CurrentCards;
import utilities.JSONProtocol.body.GameStarted;
import utilities.JSONProtocol.body.SetStartingPoint;
import utilities.JSONProtocol.body.YourCards;
import utilities.MapConverter;
import utilities.SoundHandler;
import utilities.Utilities;
import utilities.enums.AttributeType;
import utilities.enums.Orientation;
import utilities.enums.PhaseState;

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

    private PlayerMatController playerMatController;
    private ConstructionController constructionController;
    private ProgrammingController programmingController;
    private ActivationController activationController;

    private Pane constructionPane;
    private Pane programmingPane;
    private Pane activationPane;

    private SoundHandler soundHandler;
    private EventHandler<MouseEvent> onMapClicked;

    private PhaseState currentPhase = PhaseState.CONSTRUCTION;

    @FXML
    private StackPane playerMap;
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

        boardPane.addEventHandler(MOUSE_CLICKED, onMapClicked = mouseEvent -> {
            int x = (int) mouseEvent.getX() / Utilities.FIELD_SIZE;
            int y = (int) mouseEvent.getY() / Utilities.FIELD_SIZE;
            int pos = x + y * Utilities.MAP_WIDTH;
            client.sendMessage(new SetStartingPoint(pos));
        });

        this.soundHandler = new SoundHandler();
    }

    public void changeDirection() {

        ImageView robotImageView = (ImageView) fields[7][8].getChildren().get(fields[7][8].getChildren().size() - 1);
        double currentDirection = robotImageView.rotateProperty().getValue();
        robotImageView.rotateProperty().setValue(currentDirection - 90);
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

    public void handlePlayerTurning(Player player, Orientation rotation) {
        ImageView imageView = robotImageViews.get(player);
        int angle = 0;
        Robot r = player.getRobot();
        if (rotation == Orientation.LEFT) {
            angle = -90;
            r.setOrientation(r.getOrientation().getPrevious());
        } else if (rotation == Orientation.RIGHT) {
            angle = 90;
            r.setOrientation(r.getOrientation().getNext());
        }

        RotateTransition transition = new RotateTransition();
        transition.setDuration(Duration.seconds(1));
        transition.setNode(imageView);
        transition.setByAngle(angle);
        transition.play();
    }



    public ProgrammingController getProgrammingController() {
        return programmingController;
    }

    public ActivationController getActivationController() {
        return activationController;
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

    public void changePhaseView(PhaseState phase) {
        currentPhase = phase;

        switch (phase) {
            case CONSTRUCTION -> phasePane.setCenter(constructionPane);
            case PROGRAMMING -> phasePane.setCenter(programmingPane);
            case ACTIVATION -> phasePane.setCenter(activationPane);
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