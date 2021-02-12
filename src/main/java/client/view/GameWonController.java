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

    private final Game game = Game.getInstance();
    private final Server server = Server.getInstance();

    private GameController gameController;
    private final GameBoardController gameBoardController= new GameBoardController();
    private final LobbyController lobbyController = new LobbyController();

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
    public void returnClicked() throws IOException {
        //Platform.runLater(viewManager::resetGame);

        viewManager.reconstructGame();
    }




    /**
     * This method gets called by clicking on exit and exits the game
     */
    @FXML
    public void exitGameClicked() {
        System.exit(0);
    }

}
