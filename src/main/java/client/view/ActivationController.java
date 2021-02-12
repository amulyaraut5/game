package client.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.enums.CardType;
import utilities.enums.InnerActivation;

import java.io.IOException;

/**
 * This class represents the activation phase in the ui. It displays which register it is and every information related to
 * this phase. In two inner views the player can either choose to play his cards or
 * if the player gets damage.
 *
 * @author sarah
 */

public class ActivationController extends Controller {
    private static final Logger logger = LogManager.getLogger();

    /**
     * The innerview which either displays playCard or pickDamage.
     */
    public BorderPane innerActivationPhase;
    /**
     * The controller for the pickDamagePane.
     */
    public PickDamageController pickDamageController;
    /**
     * The controller for the playCardController.
     */
    public PlayCardController playCardController;
    /**
     * The pickDamagePane contains the pickDamage view to let the player select damage cards.
     */
    private Pane pickDamagePane;
    /**
     * The playCardPane contains the playCard view to let the player play programming cards.
     */
    private Pane playCardPane;
    /**
     * The counter for the current register. At the end of one round it gets set to 1 again.
     */
    private int registerNr = 1;

    /**
     * The Label displays the current register.
     */
    @FXML
    private Label register;

    /**
     * This method initializes the ActivationController, sets important things visible.
     */
    public void initialize() {
        constructPhaseViews();
        reset();
    }

    /**
     * This method resets the class and gets called at the start of activationPhase.
     */
    public void reset() {
        changePhaseView(InnerActivation.PlayIt);
        registerNr = 1;
        playCardController.getPlayItButton().setDisable(true);
    }

    /**
     * This method constructs the two innerviews and initializes the controller for them.
     */
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

    /**
     * This method changes the innerview either its pickDamage or playCard.
     *
     * @param innerActivation which should be displayed next
     */
    public void changePhaseView(InnerActivation innerActivation) {
        switch (innerActivation) {
            case Damage -> innerActivationPhase.setCenter(pickDamagePane);
            case PlayIt -> innerActivationPhase.setCenter(playCardPane);
        }
    }

    /**
     * This method gets called by getting protocol currentCards and displays current card of player
     * it also increases register number for view.
     *
     * @param cardType current card of the player
     */
    public void currentCards(CardType cardType) {
        playCardController.getCurrentCardImageView().setImage(new Image(getClass().getResource("/cards/programming/" + cardType + "-card.png").toString()));
        register.setText("Register " + registerNr);
        registerNr++;
    }

    /**
     * This method is the getter for the pickDamageController.
     *
     * @return the pickDamageController
     */
    public PickDamageController getPickDamageController() {
        return pickDamageController;
    }

    /**
     * This method is a getter for the playCardController.
     *
     * @return the playCardController
     */
    public PlayCardController getPlayCardController() {
        return playCardController;
    }
}

