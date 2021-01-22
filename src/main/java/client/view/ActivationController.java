package client.view;

import game.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utilities.JSONProtocol.body.CurrentCards;
import utilities.JSONProtocol.body.PlayIt;

import java.util.ArrayList;

public class ActivationController extends Controller {

    @FXML
    private Label register;
    @FXML
    private Label activateProgOrBoard;
    @FXML
    private Label displayResult;
    @FXML
    private Label showPriority;
    @FXML
    private ImageView imageView1;
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
}