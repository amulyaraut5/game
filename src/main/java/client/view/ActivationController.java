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
    private ImageView imageView3;

    private int registerNr = 1; //TODO reset after 5 registers

    public void initialize() {
    }

    public void currentCards(CurrentCards currentCards){
        for (int i = 0; i<2; i++){
            if (currentCards.getActiveCards().get(i).getPlayerID() == client.getThisPlayersID()){
                String card = currentCards.getActiveCards().get(i).getCard().name();
                currentCardImageView.setImage(new Image(getClass().getResource("/cards/programming/" + card + "-card.png").toString()));
                register.setText("Register " + registerNr);
            }
        }

    }
    @FXML
    private void playItButton(){
        client.sendMessage(new PlayIt());
    }

    public  void setInfoLabel(String text){
        infoLabel.setText(text);
    }
}