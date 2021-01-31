package client.view;

import game.Player;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import utilities.ImageHandler;
import javafx.scene.control.Label;

import java.awt.*;

public class GameWonController extends Controller{

    @FXML
    private Button exitGameButton;
    @FXML
    private Button newGameButton;
    @FXML
    private FlowPane winnerIconPane;
    @FXML
    private Label winnerLabel;

    public void initialize() {

    }

    public void setWinnerLabel(Player winner){
        String path = "/lobby/" + robotNames[winner.getFigure()] + ".png";
        String name = client.getUniqueName(winner.getID());
        ImageView imageView = ImageHandler.createImageView(path, 200, 200);
        winnerLabel.setText(name);

        winnerIconPane.getChildren().add(imageView);
    }

    @FXML
    public void returnClicked() {
        viewManager.closeGame(); //reset Game and Lobby
        viewManager.showLobby();
    }

    @FXML
    public void exitGameClicked() {
        viewManager.closeGame();
    }
}