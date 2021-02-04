package client.view;

import game.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.ImageHandler;
import utilities.JSONProtocol.body.PickDamage;
import utilities.JSONProtocol.body.PlayIt;
import utilities.JSONProtocol.body.SelectDamage;
import utilities.enums.CardType;
import utilities.enums.GameState;
import utilities.enums.MessageType;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author sarah
 */

public class ActivationController extends Controller {
    private static final Logger logger = LogManager.getLogger();
    private int registerNr = 1;
    @FXML
    private Label register;


    public BorderPane innerActivationPhase;
    public Pane pickDamagePane;
    public Pane playCardPane;
    public PickDamageController pickDamageController;
    public PlayCardController playCardController;

    /**
     * This method initializes the ActivationController, sets important things visible
     */
    public void initialize() {
        constructPhaseViews();
        reset();
    }

    /**
     * This method resets the class and gets called at the start of activationPhase
     */
    public void reset() {
        changePhaseView("PlayIt");
        registerNr = 1;
        playCardController.getPlayItButton().setDisable(true);
    }

    private void constructPhaseViews() {

        FXMLLoader pickDamageLoader = new FXMLLoader(getClass().getResource("/view/innerViews/pickDamage.fxml"));
        FXMLLoader playCardLoader = new FXMLLoader(getClass().getResource("/view/innerViews/playCard.fxml"));

        try {
            pickDamagePane = pickDamageLoader.load();
            playCardPane = playCardLoader.load();

            pickDamageController = pickDamageLoader.getController();
            playCardController = playCardLoader.getController();

            pickDamageController.setActivationController(this);
        } catch (IOException e) {
            logger.error("Inner phase View could not be loaded: " + e.getMessage());
        }
    }

    public void changePhaseView(String innerView) {
        switch (innerView) {
            case "Damage" -> { innerActivationPhase.setCenter(pickDamagePane);}
            case "PlayIt" -> { innerActivationPhase.setCenter(playCardPane);}
        }}


    public PickDamageController getPickDamageController() {
        return pickDamageController;
    }

    public PlayCardController getPlayCardController() {
        return playCardController;
    }


    /**
     * This method gets called by getting protocol currentCards and displays current card of player
     * it also increases register number for view
     *
     * @param cardType current card of the player
     */
    public void currentCards(CardType cardType) {
        playCardController.getCurrentCardImageView().setImage(new Image(getClass().getResource("/cards/programming/" + cardType + "-card.png").toString()));
        register.setText("Register " + registerNr);
        registerNr++;
    }



}

