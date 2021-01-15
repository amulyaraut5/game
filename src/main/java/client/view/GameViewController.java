package client.view;

import game.Game;
import game.Player;
import game.gameObjects.maps.Map;
import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Empty;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Coordinate;
import utilities.JSONProtocol.body.SetStartingPoint;
import utilities.JSONProtocol.body.YourCards;
import utilities.MapConverter;
import utilities.Utilities;
import utilities.Utilities.AttributeType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The GameViewController class controls the GameView and coordinates its inner views
 *
 * @author Simon, Sarah
 */
public class GameViewController extends Controller {
    private static final Logger logger = LogManager.getLogger();
    private final Group[][] fields = new Group[Utilities.MAP_WIDTH][Utilities.MAP_HEIGHT];
    @FXML
    public AnchorPane playerMap; //TODO make it private
    private int currentPhaseView = 0;
    private int ActivePhase = 0; //TODO enum? move to client?
    @FXML
    private BorderPane outerPane;
    @FXML
    private BorderPane chatPane;
    @FXML
    private FlowPane boardPane;

    @FXML
    public void initialize() {
        boardPane.addEventHandler(MouseEvent.MOUSE_CLICKED,
                mouseEvent -> {
                    int xMax = Utilities.MAP_WIDTH;
                    int fieldSize = Utilities.FIELD_SIZE;
                    int x = (int) mouseEvent.getX() / fieldSize;
                    int y = (int) mouseEvent.getY() / fieldSize;
                    int pos = y * xMax + x;
                    client.sendMessage(new SetStartingPoint(pos));
                    //placeRobotInMap(x,y);

                });
    }

    public Group[][] getFields() {
        return fields;
    }

    public void attachPlayerMap(Pane playerM) {
        playerM.setPrefHeight(playerM.getPrefWidth());
        playerM.setPrefHeight(playerM.getPrefHeight());
        playerMap.getChildren().add(playerM);
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

    public void buildMap(Map map) {
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
                boardPane.getChildren().add(fields[x][y]);
            }
        }
    }

    // <----------------------Only For Test---------------------------->
    public void placeRobotInMap(){

        ImageView imageView = new ImageView(new Image(getClass().getResource("/lobby/hammerbot.png").toExternalForm()));
        imageView.fitWidthProperty().bind(boardPane.widthProperty().divide(Utilities.MAP_WIDTH));
        imageView.fitHeightProperty().bind(boardPane.heightProperty().divide(Utilities.MAP_HEIGHT));
        imageView.setPreserveRatio(true);
        fields[7][8].getChildren().add(imageView);
        //fields[88].getChildren().add(new MoveI().drawCardImage());
    }

    public void changeDirection(){

        ImageView robotImageView= (ImageView) getFields()[7][8].getChildren().get(getFields()[7][8].getChildren().size()-1);
        double currentDirection = robotImageView.rotateProperty().getValue();
        robotImageView.rotateProperty().setValue(currentDirection - 90);
    }

    // Called twice will move the tile image
    public void moveRobot(){
        ImageView robotImageView = (ImageView) getFields()[7][8].getChildren().get(getFields()[7][8].getChildren().size()-1);
        getFields()[7][8].getChildren().remove(getFields()[7][8].getChildren().size()-1);
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

    public void handleMovement(int playerId, int to){
        // TODO Handle Player differently???

        for(Player player: Game.getInstance().getPlayers()) {
            if (player.getPlayerID() == playerId) {
                // Get the Robot position from the Board
                Coordinate oldRobotPosition = player.getRobot().getPosition();
                int x = oldRobotPosition.getX();
                int y = oldRobotPosition.getY();
                //int position = convertToInt(Coordinate coordinate);

                //Coordinate newRobotPosition = MapConverter.reconvertToCoordinate(to);
                //int newX = newRobotPosition.getX();
                //int newY = newRobotPosition.getY();

                // Get ImageView from the old position
                ImageView imageView = (ImageView) getFields()[x][y].getChildren().get(getFields()[x][y].getChildren().size()-1);
                // Remove the imageView from the old position
                getFields()[x][y].getChildren().remove(getFields()[x][y].getChildren().size()-1);
                // Set the imageView to new position
                // Change x and y to newX and newY
                getFields()[x][y].getChildren().add(imageView);
            }
        }
    }

    public void handlePlayerTurning(int playerID, Utilities.Rotation rotation) {
        // TODO Handle Player differently???

        for(Player player: Game.getInstance().getPlayers()){
            if(player.getPlayerID() == playerID){

                Coordinate oldRobotPosition = player.getRobot().getPosition();
                int x = oldRobotPosition.getX();
                int y = oldRobotPosition.getY();
                //int position = convertToInt(Coordinate coordinate);

                // Get the imageView from that position
                ImageView robotImageView = (ImageView) getFields()[x][y].getChildren().get(getFields()[x][y].getChildren().size()-1);
                // Direction of robotImageView
                double currentDirection = robotImageView.rotateProperty().getValue();

                //Turn the robot based on the direction
                if (rotation.equals(Utilities.Rotation.LEFT)) {
                    robotImageView.rotateProperty().setValue(currentDirection - 90);
                } else {
                    robotImageView.rotateProperty().setValue(currentDirection + 90);
                }
            }
        }
    }


    //TODO inner view with activation phase
    public void programCards(YourCards yourCards) {

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
    public void changeInnerView() {//TODO set it private
        Pane innerPane = setNextPhase();
        outerPane.setCenter(innerPane);
    }

    /**
     * @return
     */
    private Pane setNextPhase() {
        Pane innerPane = null;
        String path = "";
        currentPhaseView = ++currentPhaseView % 3;

        if (currentPhaseView == 0) path = "/view/innerViews/upgradeView.fxml";
        else if (currentPhaseView == 1) path = "/view/innerViews/programmingPhase.fxml";
        else if (currentPhaseView == 2) path = "/view/innerViews/activationView.fxml";

        try {
            innerPane = FXMLLoader.load(getClass().getResource(path));
        } catch (IOException e) {
            logger.error("Inner phase View could not be loaded: " + e.getMessage());
        }
        return innerPane;
        /*
        path = "/view/innerViews/programmingPhase.fxml";
        try {
            innerPane = FXMLLoader.load(getClass().getResource(path));
        } catch (IOException e) {
            logger.error("Inner phase View could not be loaded: " + e.getMessage());
        }
        return innerPane;

         */
    }


}