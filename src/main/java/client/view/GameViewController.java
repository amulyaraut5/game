package client.view;

import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Empty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.body.gameStarted.BoardElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The GameViewController class controls the GameView and the Chat of the game
 *
 * @author Sarah, Simon
 */
public class GameViewController extends Controller {
    private static final Logger logger = LogManager.getLogger();
    private final Group[] fields = new Group[100];
    private int currentInnerView = 0;
    /**
     * the ready Button which can be clicked to show the availability for playing the game
     */
    @FXML
    private Button readyButton;

    @FXML
    private BorderPane outerPane;

    @FXML
    private BorderPane chatPane;

    @FXML
    private FlowPane flowPane;

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
        }

        for (BoardElement tile : map) {
            int pos = tile.getPosition();
            var field = tile.getField();

            if (emptyBackground(field)) {    //If the image would be transparent, an empty tile is added.
                fields[pos - 1].getChildren().add(new Empty().createImage());
            }

            field = sortAttributes(field);
            for (Attribute attribute : field) {
                Node attributeImage = attribute.createImage();
                if (attributeImage != null) {
                    fields[pos - 1].getChildren().add(attributeImage);
                }
            }
            flowPane.getChildren().add(fields[pos - 1]);
        }
    }

    private boolean emptyBackground(ArrayList<Attribute> field) {
        for (Attribute a : field) {
            if (a.getType().equals("Pit") || a.getType().equals("Empty")) {
                return false;
            }
        }
        return true;
    }

    private ArrayList<Attribute> sortAttributes(ArrayList<Attribute> field) {
        var sortedField = new ArrayList<Attribute>();
        String[][] priorityArray = {
                {"Pit", "Empty"},
                {"Belt", "RotatingBelt", "Gear", "EnergySpace", "Antenna"},
                {"PushPanel", "Laser", "ControlPoint"},
                {"Wall"}};

        for (String[] priority : priorityArray) {
            List<String> priorityList = Arrays.asList(priority);
            for (Attribute a : field) {
                String type = a.getType();
                if (priorityList.contains(type)) {
                    sortedField.add(a);
                }
            }
        }
        return sortedField;
    }

    @FXML
    private void changeInnerView(ActionEvent event) {
        Pane innerPane = setNextPane();
        outerPane.setCenter(innerPane);
    }

    private Pane setNextPane() {
        Pane innerPane = null;
        String path = "";
        currentInnerView = ++currentInnerView % 3;

        if (currentInnerView == 0) path = "/view/innerViews/upgradeView.fxml";
        else if (currentInnerView == 1) path = "/view/innerViews/programmingView.fxml";
        else if (currentInnerView == 2) path = "/view/innerViews/activationView.fxml";

        try {
            innerPane = FXMLLoader.load(getClass().getResource(path));
        } catch (IOException e) {
            logger.error("Inner phase View could not be loaded: " + e.getMessage());
        }
        return innerPane;
    }


}