package client.view;

import game.Game;
import game.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import server.Server;
import utilities.ImageHandler;

import java.awt.*;
import java.io.IOException;

/**
 * This class is responsible for announcing the winner and ending the game.
 *
 * @author annika
 */
public class GameWonController extends Controller {

    /**
     * Button to exit the game
     */
    @FXML
    private Button exitGameButton;
    /**
     * This pane stores the ImageView of the winner
     */
    @FXML
    private FlowPane winnerIconPane;
    /**
     * This Label shows the name of the winner
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
     * This method gets called by clicking on exit and exits the game
     */
    @FXML
    public void exitGameClicked() {
        System.exit(0);
    }

}
