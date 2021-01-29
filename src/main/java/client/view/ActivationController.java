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
import utilities.JSONProtocol.body.*;
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
    private int drawDamage;
    private int registerNr = 1; //TODO reset after 5 registers


    public void reset(){
        registerNr = 1;
        playItButton.setDisable(true);
    }
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
            setInfoLabel("It's not your turn");
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
        damageInfoLabel.setText("You have to pick " + pickDamage.getCount() + " damage cards");
        this.pickDamage = pickDamage.getCount();
    }

    public void damageButtonClicked(ActionEvent actionEvent){
        CardType clickedButton = null;
        if(actionEvent.getSource().equals(spamCardButton)) clickedButton = CardType.Spam;
        if(actionEvent.getSource().equals(trojanCardButton)) clickedButton = CardType.Trojan;
        if(actionEvent.getSource().equals(virusCardButton)) clickedButton = CardType.Virus;
        if(actionEvent.getSource().equals(wormCardButton)) clickedButton = CardType.Worm;
        checkDamageReady(clickedButton);
    }

    @FXML
    private void wormCard(ActionEvent actionEvent) {
        checkDamageReady(CardType.Worm);
    }

    private void checkDamageReady(CardType card){
        pickedDamageCards.add(card);
        ImageView selectedDamageCard = new ImageView(new Image(getClass().getResource("/cards/programming/" + card + "-card.png").toString()));
        selectedDamageCard.setFitHeight(150);
        selectedDamageCard.setFitWidth(85);
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

