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

public class PlayCardController extends Controller {

    @FXML
    private ImageView currentCardImageView;
    @FXML
    private Button playItButton;
    @FXML
    private Label infoLabel;
    @FXML
    private AnchorPane playCardAnchorPane;
    public Label drawDamageLabel;
    public HBox drawDamageHBox;
    public AnchorPane drawDamageAnchorPane;

    public void initialize(){
        drawDamageHBox.setAlignment(Pos.CENTER);
        drawDamageHBox.setSpacing(5);
        drawDamageAnchorPane.setVisible(false);
    }

    public Button getPlayItButton() {
        return playItButton;
    }

    public ImageView getCurrentCardImageView() {
        return currentCardImageView;
    }

    /**
     * This method displays if player is current player and sets play It button disable (or not)
     *
     * @param turn
     */
    public void currentPlayer(boolean turn) {
        if (turn) {
            setInfoLabel("It's your turn! \n Click on the button to validate your card!");
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
     * @param drawDamage
     */
    public void setDrawDamage(DrawDamage drawDamage) {
        drawDamageHBox.getChildren().clear();

        drawDamageLabel.setText("You got damage!");
        for (int i = 0; i < drawDamage.getCards().size(); i++) {
            String path = "/cards/programming/" + drawDamage.getCards().get(i).name() + "-card.png";
            ImageView damage = new ImageView(new Image(getClass().getResource(path).toString()));
            damage.setFitWidth(30);
            damage.setFitHeight(50);
            drawDamageHBox.getChildren().add(damage);
        }

        Platform.runLater(() -> drawDamageAnchorPane.setVisible(true));
        Platform.runLater(() -> timerSchedule());
    }

    private void timerSchedule(){
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
}
