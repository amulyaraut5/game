package client.view;

import game.Game;
import game.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import server.Server;
import server.User;
import utilities.ImageHandler;
import utilities.enums.ServerState;

import java.awt.*;
import java.util.ArrayList;

public class GameWonController extends Controller {

    @FXML
    private Button exitGameButton;
    @FXML
    private Button newGameButton;
    @FXML
    private FlowPane winnerIconPane;
    @FXML
    private Label winnerLabel;

    private Game game = Game.getInstance();
    private Server server = Server.getInstance();

    private GameController gameController;

    public void setWinnerLabel(Player winner) {
        String path = "/lobby/" + robotNames[winner.getFigure()] + ".png";
        String name = client.getUniqueName(winner.getID());
        ImageView imageView = ImageHandler.createImageView(path, 200, 200);
        winnerLabel.setText(name);

        winnerIconPane.getChildren().add(imageView);
    }

    @FXML
    public void returnClicked() {
        viewManager.closeGame(); //reset Game and Lobby
        game.reset();

        //gameController.removeAll();

        server.getReadyUsers().clear();
        server.setServerState(ServerState.LOBBY);
        viewManager.showLobby();
    }

    @FXML
    public void exitGameClicked() {
        viewManager.closeGame();
    }
}
