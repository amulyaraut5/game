package client.view;

import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Empty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.body.YourCards;
import utilities.JSONProtocol.body.gameStarted.BoardElement;
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

    public void buildMap(ArrayList<BoardElement> map) {
        for (int x = 0; x < 13; x++) {
            for (int y = 0; y < 10; y++) {
                fields[x][y] = new Group();
//                fields[i].addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
//                    client.sendMessage(new SetStartingPoint(position));
//                });
            }
        }

        int pos = 0;
        for (int x = 0; x < 13; x++) {
            for (int y = 0; y < 10; y++) {
                ArrayList<Attribute> field = map.get(pos++).getField();

                if (isTileBackgroundEmpty(field)) {    //If the image would be transparent, an empty tile is added.
                    fields[x][y].getChildren().add(new Empty().createImage());
                }

                field = sortBoardAttributes(field);
                for (Attribute attribute : field) {
                    Node attributeImage = attribute.createImage();
                    if (attributeImage != null) {
                        fields[x][y].getChildren().add(attributeImage);
                    }
                }
                boardPane.getChildren().add(fields[x][y]);
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
        /*currentPhaseView = ++currentPhaseView % 3;

        if (currentPhaseView == 0) path = "/view/innerViews/upgradeView.fxml";
        else if (currentPhaseView == 1) path = "/view/innerViews/programmingView.fxml";
        else if (currentPhaseView == 2) path = "/view/innerViews/activationView.fxml";

        try {
            innerPane = FXMLLoader.load(getClass().getResource(path));
        } catch (IOException e) {
            logger.error("Inner phase View could not be loaded: " + e.getMessage());
        }
        return innerPane;*/
        path = "/view/innerViews/programmingPhase.fxml";
        try {
            innerPane = FXMLLoader.load(getClass().getResource(path));
        } catch (IOException e) {
            logger.error("Inner phase View could not be loaded: " + e.getMessage());
        }
        return innerPane;
    }


}