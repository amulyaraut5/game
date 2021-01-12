package client.view;

import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Empty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.body.SetStartingPoint;
import utilities.JSONProtocol.body.gameStarted.BoardElement;
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
    private final Group[] fields = new Group[100];
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
        for (int i = 0; i < 100; i++) {
            fields[i] = new Group();
            int position = i + 1;
            fields[i].addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                client.sendMessage(new SetStartingPoint(position));
            });
        }

        for (BoardElement tile : map) {
            int pos = tile.getPosition();
            var field = tile.getField();

            if (emptyTileBackground(field)) {    //If the image would be transparent, an empty tile is added.
                fields[pos - 1].getChildren().add(new Empty().createImage());
            }

            field = sortBoardAttributes(field);
            for (Attribute attribute : field) {
                Node attributeImage = attribute.createImage();
                if (attributeImage != null) {
                    fields[pos - 1].getChildren().add(attributeImage);
                }
            }
            boardPane.getChildren().add(fields[pos - 1]);
        }
    }

    /**
     * Method tests if the background of all attributes on a field are transparent.
     *
     * @param field Field to test
     * @return True if the Tile is transparent.
     */
    private boolean emptyTileBackground(ArrayList<Attribute> field) {
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
    private void changeInnerView() {
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
        else if (currentPhaseView == 1) path = "/view/innerViews/programmingView.fxml";
        else if (currentPhaseView == 2) path = "/view/innerViews/activationView.fxml";

        try {
            innerPane = FXMLLoader.load(getClass().getResource(path));
        } catch (IOException e) {
            logger.error("Inner phase View could not be loaded: " + e.getMessage());
        }
        return innerPane;
    }


}