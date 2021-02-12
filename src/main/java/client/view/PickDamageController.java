package client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import utilities.ImageHandler;
import utilities.JSONProtocol.body.PickDamage;
import utilities.JSONProtocol.body.SelectDamage;
import utilities.enums.CardType;
import utilities.enums.InnerActivation;

import java.util.ArrayList;

/**
 * This class represents the innerview of the activation phase for the situation that the
 * player has to pick damage cards because the spam deck is empty. The player has to pick a certain
 * amount of cards of the other damage decks and send his choice back to the server.
 */
public class PickDamageController extends Controller {
    /**
     * The ActivationController that sets this view as inner view
     */
    private ActivationController activationController;
    /**
     * The amount of damage cards the player should pick.
     */
    private int pickDamage;
    /**
     * It saves the actual count of spam cards in the spam deck.
     */
    private int countSpam;
    /**
     * It saves the actual count of trojan cards in the trojan deck.
     */
    private int countTrojan;
    /**
     * It saves the actual count of virus cards in the virus deck.
     */
    private int countVirus;
    /**
     * It saves the actual count of worm cards in the worm deck.
     */
    private int countWorm;
    /**
     * This list stores all damage cards that the player chose and gets cleared when one pickDamage pass is over.
     */
    private final ArrayList<CardType> pickedDamageCards = new ArrayList<>();
    /**
     * This HBox shows the images of damage cards the player has chosen.
     */
    @FXML
    private HBox selectedDamageHBox;
    /**
     * The Button for choosing a spam card.
     */
    @FXML
    private Button spamCardButton;
    /**
     * The Button for choosing a trojan card.
     */
    @FXML
    private Button trojanCardButton;
    /**
     * The Button for choosing a virus card.
     */
    @FXML
    private Button virusCardButton;
    /**
     * The Button for choosing a worm card.
     */
    @FXML
    private Button wormCardButton;
    /**
     * This Label displays how much cards the player should pick.
     */
    @FXML
    private Label damageInfoLabel;

    public void initialize(){
        selectedDamageHBox.setSpacing(20);
    }

    /**
     * By getting protocol PickDamage the damageAnchorPane gets visible and the method updates the
     * count numbers on the different damageButtons, shows how much damage cards the player has to choose and
     * stores the current counts of each damage deck in its class attributes.
     *
     * @param pickDamage how much damage cards the player has to select
     */
    public void pickDamage(PickDamage pickDamage) {
        countSpam = viewClient.getCountSpamCards();
        countTrojan = viewClient.getCountTrojanCards();
        countVirus = viewClient.getCountVirusCards();
        countWorm = viewClient.getCountWormCards();
        updateDamageCountLabel(CardType.Spam);
        updateDamageCountLabel(CardType.Worm);
        updateDamageCountLabel(CardType.Trojan);
        updateDamageCountLabel(CardType.Virus);

        selectedDamageHBox.getChildren().clear();
        damageInfoLabel.setText("You have to pick " + pickDamage.getCount() + " damage cards");
        this.pickDamage = pickDamage.getCount();
    }

    /**
     * This method updates displaying the count of cards each button should represent and disables the buttons
     * if the deck is empty
     *
     * @param cardType the CardType the player selected
     */
    private void updateDamageCountLabel(CardType cardType) {
        switch (cardType) {
            case Spam -> {
                spamCardButton.setText(String.valueOf(countSpam));
                spamCardButton.setDisable(countSpam == 0);
            }
            case Trojan -> {
                trojanCardButton.setText(String.valueOf(countTrojan));
                trojanCardButton.setDisable(countTrojan == 0);
            }
            case Virus -> {
                virusCardButton.setText(String.valueOf(countVirus));
                virusCardButton.setDisable(countVirus == 0);
            }
            case Worm -> {
                wormCardButton.setText(String.valueOf(countWorm));
                wormCardButton.setDisable(countWorm == 0);
            }
        }
    }

    /**
     *This method is called when one of the four buttons is pressed.
     * Depending on which button this was, the number of cards in the locally stored class attribute
     * that represents this deck is increased and the method checkDamageReady is called with the selected CardType.
     *
     * @param actionEvent the actionEvent which also stores its source
     */
    @FXML
    private void damageButtonClicked(ActionEvent actionEvent) {
        CardType clickedButton = null;
        if (actionEvent.getSource().equals(spamCardButton)) {
            clickedButton = CardType.Spam;
            countSpam--;
        } else if (actionEvent.getSource().equals(trojanCardButton)) {
            clickedButton = CardType.Trojan;
            countTrojan--;
        } else if (actionEvent.getSource().equals(virusCardButton)) {
            clickedButton = CardType.Virus;
            countVirus--;
        } else if (actionEvent.getSource().equals(wormCardButton)) {
            clickedButton = CardType.Worm;
            countWorm--;
        }
        checkDamageReady(clickedButton);
    }

    /**
     * This method checks if the player picked as much cards as he should and displays the selected card
     * underneath the buttons. If the player picked enough cards everything related to displaying this class
     * gets reset and the innerView of the activation phase gets changed back to Play It.
     *
     * @param card the CardType the player selected
     */
    private void checkDamageReady(CardType card) {
        updateDamageCountLabel(card);
        pickedDamageCards.add(card);
        String path = "/cards/programming/" + card + "-card.png";
        ImageView selectedDamageCard = ImageHandler.createImageView(path, 70, 100);
        selectedDamageHBox.getChildren().add(selectedDamageCard);
        if (pickedDamageCards.size() == pickDamage) {
            viewClient.sendMessage(new SelectDamage(pickedDamageCards));
            pickDamage = 0;
            pickedDamageCards.clear();
            activationController.changePhaseView(InnerActivation.PlayIt);
        }
    }


    public void setActivationController(ActivationController activationController) {
        this.activationController = activationController;
    }
}
