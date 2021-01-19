package client.view;

import client.model.Client;
import game.Player;
import game.gameObjects.maps.Map;
import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Empty;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Coordinate;
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
        Map map = MapConverter.reconvert(gameStarted);
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

    public void placeRobotInMap(Player player, int position) {
        if (player.getID() == client.getThisPlayersID()) {
            boardPane.removeEventHandler(MOUSE_CLICKED, onMapClicked);
        }

        Coordinate newRobotPosition = Coordinate.parse(position);
        int newX = newRobotPosition.getX();
        int newY = newRobotPosition.getY();
        ImageView imageView = player.getRobot().drawRobotImage();

        imageView.fitWidthProperty().bind(boardPane.widthProperty().divide(Utilities.MAP_WIDTH));
        imageView.fitHeightProperty().bind(boardPane.heightProperty().divide(Utilities.MAP_HEIGHT));
        imageView.setPreserveRatio(true);

        robotPane.getChildren().add(imageView);
        imageView.setX(newX * Utilities.FIELD_SIZE);
        imageView.setY(newY * Utilities.FIELD_SIZE);
    }
    // <----------------------Only For Test to show Robot movement by translate transition---------------------------->
    public void tempRobot(){
        int newX = 7;
        int newY = 8;
        Image image = new Image(getClass().getResource("/lobby/hammerbot.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.fitWidthProperty().bind(boardPane.widthProperty().divide(Utilities.MAP_WIDTH));
        imageView.fitHeightProperty().bind(boardPane.heightProperty().divide(Utilities.MAP_HEIGHT));
        imageView.setPreserveRatio(true);

        robotPane.getChildren().add(imageView);
        imageView.setX(newX * Utilities.FIELD_SIZE);
        imageView.setY(newY * Utilities.FIELD_SIZE);
    }

    public void moveRobot() {
        int x = 7;
        int y = 8;
        ImageView imageView = (ImageView) robotPane.getChildren().get(fields[x][y].getChildren().size() - 1);
        int newX = 1;
        int newY = 3;
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(2));
        transition.setToX(newX *Utilities.FIELD_SIZE - x* Utilities.FIELD_SIZE);
        transition.setToY(newY * Utilities.FIELD_SIZE - y*Utilities.FIELD_SIZE);
        transition.setNode(imageView);
        //transition.setInterpolator(Interpolator.LINEAR);
        transition.play();
    }
    // <----------------------Only For Test---------------------------->

    public void handleMovement(int playerId, int to) {
        // TODO Handled Player from Client

        for (Player player : Client.getInstance().getPlayers()) {
            if (player.getID() == playerId) {
                // Get the Robot position from the Board
                Coordinate oldRobotPosition = player.getRobot().getPosition();
                int x = oldRobotPosition.getX();
                int y = oldRobotPosition.getY();

                Coordinate newRobotPosition = Coordinate.parse(to);
                int newX = newRobotPosition.getX();
                int newY = newRobotPosition.getY();

                // Get ImageView from the old position
                ImageView imageView = (ImageView) fields[x][y].getChildren().get(fields[x][y].getChildren().size() - 1);
                // Remove the imageView from the old position
                fields[x][y].getChildren().remove(fields[x][y].getChildren().size() - 1);
                // Set the imageView to new position

                fields[newX][newY].getChildren().add(imageView);
            }
        }
    }

    public void handlePlayerTurning(int playerID, Orientation rotation) {
        // TODO Handled Player from Client

        for (Player player : Client.getInstance().getPlayers()) {
            if (player.getID() == playerID) {

                Coordinate oldRobotPosition = player.getRobot().getPosition();
                int x = oldRobotPosition.getX();
                int y = oldRobotPosition.getY();

                // Get the imageView from that position
                ImageView robotImageView = (ImageView) fields[x][y].getChildren().get(fields[x][y].getChildren().size() - 1);
                // Direction of robotImageView
                double currentDirection = robotImageView.rotateProperty().getValue();

                //Turn the robot based on the direction
                if (rotation.equals(Orientation.LEFT)) {
                    robotImageView.rotateProperty().setValue(currentDirection - 90);
                } else {
                    robotImageView.rotateProperty().setValue(currentDirection + 90);
                }
            }
        }
    }


    //TODO inner view with activation phase

    public void programCards(YourCards yourCards) {
        programmingController.startProgrammingPhase(yourCards.getCards());
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
}