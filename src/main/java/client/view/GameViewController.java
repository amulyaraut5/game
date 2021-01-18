package client.view;

import client.model.Client;
import game.Player;
import game.gameObjects.maps.Map;
import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Empty;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.*;
import utilities.JSONProtocol.body.GameStarted;
import utilities.JSONProtocol.body.SetStartingPoint;
import utilities.JSONProtocol.body.YourCards;
import utilities.MapConverter;
import utilities.SoundHandler;
import utilities.Utilities;
import utilities.enums.AttributeType;
import utilities.enums.Orientation;

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
    private static LobbyController lobbyController;
    private final Group[][] fields = new Group[Utilities.MAP_WIDTH][Utilities.MAP_HEIGHT];
    @FXML
    private StackPane playerMap;

    private int currentPhaseView = 0;
    private int ActivePhase = 0; //TODO enum? move to client?
    @FXML
    private BorderPane outerPane;
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
    private PlayerMapController playerMapController;

    private UpgradeController upgradeController;
    private ProgrammingController programmingController;
    private ActivationController activationController;

    private SoundHandler soundHandler;
    private EventHandler<MouseEvent> onMapClicked;

    public static void setLobbyController(LobbyController lobbyController) {
        GameViewController.lobbyController = lobbyController;
    }

    public PlayerMapController getPlayerMapController() {
        return playerMapController;
    }

    public void setPlayerMapController(PlayerMapController playerMapController) {
        this.playerMapController = playerMapController;
    }

    @FXML
    public void initialize() {
        boardPane.addEventHandler(MOUSE_CLICKED, onMapClicked = mouseEvent -> {
            int x = (int) mouseEvent.getX() / Utilities.FIELD_SIZE;
            int y = (int) mouseEvent.getY() / Utilities.FIELD_SIZE;
            int pos = x + y * Utilities.MAP_WIDTH;
            client.sendMessage(new SetStartingPoint(pos));
        });

        this.soundHandler = new SoundHandler();
    }

    public void changeDirection() {

        ImageView robotImageView = (ImageView) getFields()[7][8].getChildren().get(getFields()[7][8].getChildren().size() - 1);
        double currentDirection = robotImageView.rotateProperty().getValue();
        robotImageView.rotateProperty().setValue(currentDirection - 90);
    }

    public Group[][] getFields() {
        return fields;
    }


    public void attachPlayerMap(Pane playerM) {
        //playerM.setPrefHeight(playerMap.getPrefWidth());
        //playerM.setPrefWidth(playerMap.getPrefHeight());
        playerMap.setAlignment(Pos.CENTER);
        playerMap.getChildren().add(playerM);
        //playerMapController.loadPlayerMap();
    }


    public void attachChatPane(Pane chat) {
        chat.setPrefWidth(chatPane.getPrefWidth());
        chat.setPrefHeight(chatPane.getPrefHeight());
        chatPane.setCenter(chat);
    }

    /**
     *
     */
    public void close() {
        //client.disconnect(); TODO disconnect client on closure of window
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

    // <----------------------Only For Test---------------------------->


    public void placeRobotInMap(int playerID, int position) {

        Coordinate newRobotPosition = MapConverter.reconvertToCoordinate(position);
        int newX = newRobotPosition.getX();
        int newY = newRobotPosition.getY();
        Image image = lobbyController.getImageHashmap().get(playerID);
        ImageView imageView = new ImageView(image);

        imageView.fitWidthProperty().bind(boardPane.widthProperty().divide(Utilities.MAP_WIDTH));
        imageView.fitHeightProperty().bind(boardPane.heightProperty().divide(Utilities.MAP_HEIGHT));
        imageView.setPreserveRatio(true);

        robotPane.getChildren().add(imageView);
        imageView.setX(newX * Utilities.FIELD_SIZE);
        imageView.setY(newY * Utilities.FIELD_SIZE);
        //boardPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
    }

    // Called twice will move the tile image
    public void moveRobot() {

        ImageView robotImageView = (ImageView) getFields()[7][8].getChildren().get(getFields()[7][8].getChildren().size() - 1);
        getFields()[7][8].getChildren().remove(getFields()[7][8].getChildren().size() - 1);
        getFields()[7][6].getChildren().add(robotImageView);

        /*TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(2));
        transition.setToX(35);
        transition.setToY(45);
        transition.setNode(robotImageView);
        transition.play();
         */
    }
    // <----------------------Only For Test---------------------------->

    public void handleMovement(int playerId, int to) {
        // TODO Handled Player from Client

        for (Player player : Client.getInstance().getPlayers()) {
            if (player.getID() == playerId) {
                // Get the Robot position from the Board
                Coordinate oldRobotPosition = player.getRobot().getOldPosition();
                int x = oldRobotPosition.getX();
                int y = oldRobotPosition.getY();

                Coordinate newRobotPosition = MapConverter.reconvertToCoordinate(to);
                int newX = newRobotPosition.getX();
                int newY = newRobotPosition.getY();

                // Get ImageView from the old position
                ImageView imageView = (ImageView) getFields()[x][y].getChildren().get(getFields()[x][y].getChildren().size() - 1);
                // Remove the imageView from the old position
                getFields()[x][y].getChildren().remove(getFields()[x][y].getChildren().size() - 1);
                // Set the imageView to new position

                getFields()[newX][newY].getChildren().add(imageView);
            }
        }
    }

    public void handlePlayerTurning(int playerID, Orientation rotation) {
        // TODO Handled Player from Client

        for (Player player : Client.getInstance().getPlayers()) {
            if (player.getID() == playerID) {

                Coordinate oldRobotPosition = player.getRobot().getOldPosition();
                int x = oldRobotPosition.getX();
                int y = oldRobotPosition.getY();

                // Get the imageView from that position
                ImageView robotImageView = (ImageView) getFields()[x][y].getChildren().get(getFields()[x][y].getChildren().size() - 1);
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
                {AttributeType.PushPanel, AttributeType.Laser},
                {AttributeType.ControlPoint, AttributeType.RestartPoint, AttributeType.StartPoint},
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
    private void changeInnerView() throws IOException {
        Pane innerPane = setNextPhase();
        outerPane.setCenter(innerPane);
    }

    /**
     * @return
     */
    private Pane setNextPhase() throws IOException {
        Pane innerPane = null;
        String path = "";
        currentPhaseView = ++currentPhaseView % 3;


        if (currentPhaseView == 0){
            FXMLLoader upgradeLoader = new FXMLLoader(getClass().getResource("/view/innerViews/upgradeView.fxml"));
            try {
                innerPane = upgradeLoader.load();
            } catch (IOException e) {
                logger.error("Inner phase View could not be loaded: " + e.getMessage());
            }
            upgradeController = upgradeLoader.getController();
        } else if (currentPhaseView == 1) {
            FXMLLoader programmingLoader = new FXMLLoader(getClass().getResource("/view/innerViews/programmingPhaseView.fxml"));
            try {
                innerPane = programmingLoader.load();
            } catch (IOException e) {
                logger.error("Inner phase View could not be loaded: " + e.getMessage());
            }
            programmingController = programmingLoader.getController();
        }
        else if (currentPhaseView == 2) {
            FXMLLoader activationLoader = new FXMLLoader(getClass().getResource("/view/innerViews/activationView.fxml"));
            try {
                innerPane = activationLoader.load();
            } catch (IOException e) {
                logger.error("Inner phase View could not be loaded: " + e.getMessage());
            }
            activationController = activationLoader.getController();
        }
        /*try {
            innerPane = FXMLLoader.load(getClass().getResource(path));
        } catch (IOException e) {
            logger.error("Inner phase View could not be loaded: " + e.getMessage());
        }*/
        return innerPane;

    }


    public void soundsOnAction(javafx.event.ActionEvent event) {
        this.soundHandler.musicOn();
    }

    public void soundsOffAction(javafx.event.ActionEvent event) {
        this.soundHandler.musicOff();
    }
}