package client.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import utilities.JSONProtocol.body.DrawDamage;
import utilities.JSONProtocol.body.PlayIt;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This method represents one inner view of the activation controller.
 * It shows the part of the activation phase where the player plays the programming card and has to click on play it.
 *
 * @author sarah
 */
public class PlayCardController extends Controller {

    /**
     * This Label shows how much damage cards the player had to draw
     */
    @FXML
    private Label drawDamageLabel;

    /**
     * This HBox shows images of the damage cards the player got.
     */
    @FXML
    private HBox drawDamageHBox;

    /**
     * This Pane shows a speech bubble containing the damage information if the player got damage.
     */
    @FXML
    private AnchorPane drawDamageAnchorPane;
    /**
     * This ImageView shows an image of the programming card the player has in current register
     */
    @FXML
    private ImageView currentCardImageView;
    /**
     * The play It button to validate playing the card.
     */
    @FXML
    private Button playItButton;

    /**
     * The infoLabel displays if it's the players' turn
     */
    @FXML
    private Label infoLabel;

    /**
     * This method sets all view layout settings that are independent from the game.
     */
    public void initialize() {
        drawDamageHBox.setAlignment(Pos.CENTER);
        drawDamageHBox.setSpacing(5);
        drawDamageAnchorPane.setVisible(false);
    }

    /**
     * This method displays if player is current player and sets play It button disable (or not)
     *
     * @param turn if the player is currentPlayer
     */
    public void currentPlayer(boolean turn) {
        if (turn) {
            setInfoLabel("It's your turn! \nClick on the button \nto validate your card!");
            playItButton.setDisable(false);
        } else {
            setInfoLabel("It's not your turn");
            playItButton.setDisable(true);
        }
    }

    /**
     * This method gets called by clicking the playIt Button and sends the protocol Play It.
     */
    @FXML
    private void playItButton() {
        viewClient.sendMessage(new PlayIt());
    }

    /**
     * This method makes the damage speech bubble visible and displays how much damage the player got.
     *
     * @param drawDamage what and how much CardTypes the player got as damage
     */
    public void setDrawDamage(DrawDamage drawDamage) {
        drawDamageHBox.getChildren().clear();
        drawDamageLabel.setText("You got damage!");
        for (int i = 0; i < drawDamage.getCards().size(); i++) {
            String path = "/cards/programming/" + drawDamage.getCards().get(i).name() + "-card.png";
            ImageView damage = generateImageView(path, 30, 50);
            drawDamageHBox.getChildren().add(damage);
        }
        Platform.runLater(() -> drawDamageAnchorPane.setVisible(true));
        Platform.runLater(this::timerSchedule);
    }

    /**
     * This method shows the damageAnchorPane only for a short amount of time.
     */
    private void timerSchedule() {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if (drawDamageAnchorPane.isVisible()) {
                    Platform.runLater(() -> drawDamageAnchorPane.setVisible(false));
                }
                t.cancel();
            }
        }, 2000);
    }

    public Button getPlayItButton() {
        return playItButton;
    }

    public ImageView getCurrentCardImageView() {
        return currentCardImageView;
    }

    private void setInfoLabel(String text) {
        infoLabel.setText(text);
    }
}
