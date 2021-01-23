package client.view;

import game.Player;
import game.gameObjects.cards.DamageCard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import utilities.JSONProtocol.body.CurrentCards;
import utilities.JSONProtocol.body.PickDamage;
import utilities.JSONProtocol.body.PlayIt;
import utilities.JSONProtocol.body.SelectDamage;
import utilities.enums.CardType;

import java.util.ArrayList;

public class ActivationController extends Controller {

    @FXML
    private Label register;
    public Label infoLabel;

    @FXML
    private ImageView currentCardImageView;
    @FXML
    private HBox selectedDamageHBox;
    public Button playItButton;
    @FXML
    private AnchorPane selectDamageAnchorPane;
    @FXML
    private AnchorPane playCardAnchorPane;

    private ArrayList<CardType> pickedDamageCards = new ArrayList<>();
    private int pickDamage;
    private int registerNr = 1; //TODO reset after 5 registers

    public void initialize() {
        selectDamageAnchorPane.setVisible(false);
        playItButton.setDisable(true);

    }


    public void currentCards(CardType cardType){
        currentCardImageView.setImage(new Image(getClass().getResource("/cards/programming/" + cardType + "-card.png").toString()));
        register.setText("Register " + registerNr);

        registerNr ++;

    }

    public void currentPlayer(boolean turn){
        if(turn){
            setInfoLabel("It's your turn! Click on the button to validate your card!");
            playItButton.setDisable(false);
        } else {
            setInfoLabel("It's your turn! Click on the button to validate your card!");
            playItButton.setDisable(true);
        }

    }
    @FXML
    private void playItButton(){
        client.sendMessage(new PlayIt());
    }

    public  void setInfoLabel(String text){
        infoLabel.setText(text);
    }

    public void pickDamage(PickDamage pickDamage){
        playCardAnchorPane.setVisible(false);
        selectDamageAnchorPane.setVisible(true);
        setInfoLabel("You have to pick " + pickDamage.getCount() + " damage cards");
        this.pickDamage = pickDamage.getCount();
    }

    @FXML
    private void spamCard(ActionEvent actionEvent) {
        checkDamageReady(CardType.Spam);
    }

    @FXML
    private void trojanCard(ActionEvent actionEvent) {
        checkDamageReady(CardType.Trojan);
    }

    @FXML
    private void virusCard(ActionEvent actionEvent) {
        checkDamageReady(CardType.Virus);
    }

    @FXML
    private void wormCard(ActionEvent actionEvent) {
        checkDamageReady(CardType.Worm);
    }

    private void checkDamageReady(CardType card){
        pickedDamageCards.add(card);
        ImageView selectedDamageCard = new ImageView(new Image(getClass().getResource("/cards/programming/" + card + "-card.png").toString()));
        selectedDamageCard.setFitHeight(150);
        selectedDamageCard.setFitWidth(70);
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