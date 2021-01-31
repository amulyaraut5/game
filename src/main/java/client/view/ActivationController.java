package client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import utilities.JSONProtocol.body.*;
import utilities.enums.CardType;
import java.util.ArrayList;

/**
 *
 * @author sarah
 */

public class ActivationController extends Controller {

    @FXML
    private Label register;
    @FXML
    private Label infoLabel;
    @FXML
    private ImageView currentCardImageView;
    @FXML
    private HBox selectedDamageHBox;
    @FXML
    private Button playItButton;
    @FXML
    private AnchorPane selectDamageAnchorPane;
    @FXML
    private AnchorPane playCardAnchorPane;
    @FXML
    private Button spamCardButton;
    @FXML
    private Button trojanCardButton;
    @FXML
    private Button virusCardButton;
    @FXML
    private Button wormCardButton;
    @FXML
    private Label damageInfoLabel;
    private GameController gameController;
    private ArrayList<CardType> pickedDamageCards = new ArrayList<>();
    private int pickDamage;
    private int registerNr = 1;

    /**
     * This method resets the class and gets called at the start of activationPhase
     */
    public void reset(){
        registerNr = 1;
        playItButton.setDisable(true);
    }

    /**
     * This method initializes the ActivationController, sets important things visible
     */
    public void initialize() {
        selectDamageAnchorPane.setVisible(false);
        playItButton.setDisable(true);
    }

    /**
     * This method gets called by getting protocol currentCards and displays current card of player
     * it also increases register number for view
     * @param cardType current card of the player
     */
    public void currentCards(CardType cardType){
        currentCardImageView.setImage(new Image(getClass().getResource("/cards/programming/" + cardType + "-card.png").toString()));
        register.setText("Register " + registerNr);
        registerNr ++;
    }

    /**
     * This method displays if player is current player and sets play It button disable (or not)
     * @param turn
     */
    public void currentPlayer(boolean turn){
        if(turn){
            setInfoLabel("It's your turn! Click on the button to validate your card!");
            playItButton.setDisable(false);
        } else {
            setInfoLabel("It's not your turn");
            playItButton.setDisable(true);
        }

    }
    @FXML
    private void playItButton(){
        client.sendMessage(new PlayIt());
    }


    private void setInfoLabel(String text){
        infoLabel.setText(text);
    }

    /**
     * by getting protocol PickDamage the damageAnchorPane gets visible and it updates the
     * count numbers on the different damageButtons
     * @param pickDamage
     * @param game gameController that called this method
     */
    public void pickDamage(PickDamage pickDamage, GameController game){
        this.gameController = game;
        playCardAnchorPane.setVisible(false);
        selectDamageAnchorPane.setVisible(true);
        updateDamageCountLabel();
        damageInfoLabel.setText("You have to pick " + pickDamage.getCount() + " damage cards");
        this.pickDamage = pickDamage.getCount();
    }


    @FXML
    private void damageButtonClicked(ActionEvent actionEvent){
        CardType clickedButton = null;
        if(actionEvent.getSource().equals(spamCardButton)) clickedButton = CardType.Spam;
        if(actionEvent.getSource().equals(trojanCardButton)) clickedButton = CardType.Trojan;
        if(actionEvent.getSource().equals(virusCardButton)) clickedButton = CardType.Virus;
        if(actionEvent.getSource().equals(wormCardButton)) clickedButton = CardType.Worm;
        checkDamageReady(clickedButton);
    }

    private void updateDamageCountLabel(){
        spamCardButton.setText(String.valueOf(gameController.getCountSpamCards()));
        if(gameController.getCountSpamCards() == 0) spamCardButton.setDisable(true);

        trojanCardButton.setText(String.valueOf(gameController.getCountTrojanCards()));
        if(gameController.getCountTrojanCards() == 0) trojanCardButton.setDisable(true);

        virusCardButton.setText(String.valueOf(gameController.getCountVirusCards()));
        if(gameController.getCountVirusCards() == 0) virusCardButton.setDisable(true);

        wormCardButton.setText(String.valueOf(gameController.getCountWormCards()));
        if(gameController.getCountWormCards() == 0) wormCardButton.setDisable(true);

    }

    private void checkDamageReady(CardType card){
        gameController.handleDamageCount(card);
        updateDamageCountLabel();
        pickedDamageCards.add(card);
        ImageView selectedDamageCard = new ImageView(new Image(getClass().getResource("/cards/programming/" + card + "-card.png").toString()));
        selectedDamageCard.setFitHeight(150);
        selectedDamageCard.setFitWidth(95);
        selectedDamageHBox.getChildren().add(selectedDamageCard);
        if(pickedDamageCards.size()==pickDamage){
            client.sendMessage(new SelectDamage(pickedDamageCards));
            pickDamage = 0;
            pickedDamageCards.clear();
            playCardAnchorPane.setVisible(true);
            selectDamageAnchorPane.setVisible(false);
        }
    }
}

