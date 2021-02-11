package client.view;

import game.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import utilities.ImageHandler;

import java.awt.*;

/**
 * This class is responsible for announcing the winner and ending the game.
 *
 * @author TODO
 */
public class GameWonController extends Controller {

    @FXML
    private Button exitGameButton; //TODO
    @FXML
    private Button newGameButton;//TODO

    /**
     * This pane stores the ImageView of the winner
     */
    @FXML
    private FlowPane winnerIconPane;
    /**
     * This Label shows thee name of the winner
     */
    @FXML
    private Label winnerLabel;

    /**
     * This method creates the ImageView and Label of the winner and adds it to the view
     *
     * @param winner of the game
     */
    public void setWinnerLabel(Player winner) {
        String path = "/lobby/" + robotNames[winner.getFigure()] + ".png";
        String name = viewClient.getUniqueName(winner.getID());
        ImageView imageView = ImageHandler.createImageView(path, 200, 200);
        winnerLabel.setText(name);

        winnerIconPane.getChildren().add(imageView);
    }

    /**
     * TODO
     */
    @FXML
    public void returnClicked() {
        Platform.runLater(viewManager::resetGame);
    }

    /**
     * This method gets called by clicking on exit and exits the game
     */
    @FXML
    public void exitGameClicked() {
        System.exit(0);
    }
}
