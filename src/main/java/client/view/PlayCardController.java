package client.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import utilities.JSONProtocol.body.PlayIt;
import utilities.enums.MessageType;

import java.util.ArrayList;

public class PlayCardController extends Controller {

    public Label moveInfo;
    public Label displayAction;
    @FXML
    private ImageView currentCardImageView;
    @FXML
    private Button playItButton;
    @FXML
    private Label infoLabel;
    @FXML
    private AnchorPane playCardAnchorPane;

    public Button getPlayItButton() {
        return playItButton;
    }

    public ImageView getCurrentCardImageView() {
        return currentCardImageView;
    }

    public void setDisplayAction(ArrayList<MessageType> currentAction) {
        StringBuilder text = new StringBuilder(" ");
        for (MessageType message : currentAction) {
            text.append(message);
            text.append(" ");
        }
        String displayAction = "";
        switch (text.toString()) {
            case " Movement Movement Movement " -> displayAction = "You moved 3";
            case " Movement Movement " -> displayAction = "You moved 2";
            case " Movement " -> displayAction = "You moved 1";
            case " PlayerTurning " -> displayAction = "You turned";
            case " PlayerTurning PlayerTurning " -> displayAction = "You performed an UTurn";
            case " Energy " -> displayAction = "You got energy";
            default -> displayAction = text.toString();
        }
        moveInfo.setText(displayAction);
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
}
