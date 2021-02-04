package client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import utilities.ImageHandler;
import utilities.JSONProtocol.body.PickDamage;
import utilities.JSONProtocol.body.SelectDamage;
import utilities.enums.CardType;

import java.util.ArrayList;

public class PickDamageController extends Controller{

    @FXML
    private HBox selectedDamageHBox;

    @FXML
    private AnchorPane selectDamageAnchorPane;
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

    private final ArrayList<CardType> pickedDamageCards = new ArrayList<>();
    private int pickDamage;

    private ActivationController activationController;

    public void setActivationController(ActivationController activationController) {
        this.activationController = activationController;
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
            activationController.changePhaseView("PlayIt");
        }
    }
}
