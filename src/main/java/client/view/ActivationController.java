package client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import utilities.ImageHandler;
import utilities.JSONProtocol.body.PickDamage;
import utilities.JSONProtocol.body.PlayIt;
import utilities.JSONProtocol.body.SelectDamage;
import utilities.enums.CardType;

import java.util.ArrayList;

/**
 * @author sarah
 */

public class ActivationController extends Controller {
    private final ArrayList<CardType> pickedDamageCards = new ArrayList<>();
    private int pickDamage;
    private int registerNr = 1;

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

    /**
     * This method resets the class and gets called at the start of activationPhase
     */
    public void reset() {
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
     *
     * @param cardType current card of the player
     */
    public void currentCards(CardType cardType) {
        currentCardImageView.setImage(new Image(getClass().getResource("/cards/programming/" + cardType + "-card.png").toString()));
        register.setText("Register " + registerNr);
        registerNr++;
    }

    /**
     * This method displays if player is current player and sets play It button disable (or not)
     *
     * @param turn
     */
    public void currentPlayer(boolean turn) {
        if (turn) {
            setInfoLabel("It's your turn! Click on the button to validate your card!");
            playItButton.setDisable(false);
        } else {
            setInfoLabel("It's not your turn");
            playItButton.setDisable(true);
        }
    }

    @FXML
    private void playItButton() {
        client.sendMessage(new PlayIt());
    }

    private void setInfoLabel(String text) {
        infoLabel.setText(text);
    }

    /**
     * by getting protocol PickDamage the damageAnchorPane gets visible and it updates the
     * count numbers on the different damageButtons
     *
     * @param pickDamage
     */
    public void pickDamage(PickDamage pickDamage) {
        countSpam = getCountSpamCards();
        countTrojan = getCountTrojanCards();
        countVirus = getCountVirusCards();
        countWorm = getCountWormCards();
        updateDamageCountLabel(CardType.Spam);
        updateDamageCountLabel(CardType.Worm);
        updateDamageCountLabel(CardType.Trojan);
        updateDamageCountLabel(CardType.Virus);

        selectedDamageHBox.getChildren().clear();
        playCardAnchorPane.setVisible(false);
        selectDamageAnchorPane.setVisible(true);
        damageInfoLabel.setText("You have to pick " + pickDamage.getCount() + " damage cards");
        this.pickDamage = pickDamage.getCount();
    }

    private int countSpam;
    private int countTrojan;
    private int countVirus;
    private int countWorm;


    private void updateDamageCountLabel(CardType cardType){
        switch (cardType){
            case Spam->{
                spamCardButton.setText(String.valueOf(countSpam));
                if ((countSpam == 0)) spamCardButton.setDisable(true);
                else spamCardButton.setDisable(false);
            }
            case Trojan -> {
                trojanCardButton.setText(String.valueOf(countTrojan));
                if (countTrojan == 0) trojanCardButton.setDisable(true);
                else trojanCardButton.setDisable(false);
            }
            case Virus -> {
                virusCardButton.setText(String.valueOf(countVirus));
                if (countVirus == 0) virusCardButton.setDisable(true);
                else  virusCardButton.setDisable(false);
            }
            case Worm -> {
                wormCardButton.setText(String.valueOf(countWorm));
                if (countWorm == 0) wormCardButton.setDisable(true);
                else wormCardButton.setDisable(false);
            }
        }
    }
    @FXML
    private void damageButtonClicked(ActionEvent actionEvent) {
        CardType clickedButton = null;
        if (actionEvent.getSource().equals(spamCardButton)) {
            clickedButton = CardType.Spam;
            countSpam--;
        }
        else if (actionEvent.getSource().equals(trojanCardButton))  {
            clickedButton = CardType.Trojan;
            countTrojan--;
        }
        else if (actionEvent.getSource().equals(virusCardButton)) {
            clickedButton = CardType.Virus;
            countVirus--;
        }
        else if (actionEvent.getSource().equals(wormCardButton)) {
            clickedButton = CardType.Worm;
            countWorm--;
        }
        checkDamageReady(clickedButton);
    }

    private void checkDamageReady(CardType card) {
        updateDamageCountLabel(card);
        pickedDamageCards.add(card);
        String path = "/cards/programming/" + card + "-card.png";
        ImageView selectedDamageCard = ImageHandler.createImageView(path, 70, 100);
        selectedDamageHBox.getChildren().add(selectedDamageCard);
        if (pickedDamageCards.size() == pickDamage) {
            client.sendMessage(new SelectDamage(pickedDamageCards));
            pickDamage = 0;
            pickedDamageCards.clear();
            playCardAnchorPane.setVisible(true);
            selectDamageAnchorPane.setVisible(false);
        }
    }
}

