package client.view;

import game.Game;
import game.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import server.Server;
import utilities.ImageHandler;
import utilities.JSONProtocol.body.SetStatus;
import utilities.enums.ServerState;

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

    private Game game = Game.getInstance();
    private Server server = Server.getInstance();

    private GameController gameController;
    private GameBoardController gameBoardController= new GameBoardController();
    private LobbyController lobbyController = new LobbyController();

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
        Player player = viewClient.getPlayerFromID(viewClient.getThisPlayersID());
        //gameController.removePlayer(player);


        viewManager.closeGame(); //reset Game and Lobby

        viewManager.resetPlayer(player);

        game.reset();


        //gameBoardController.removeAll();
        ;

        //gameController.removeAll();
        //gameBoardController.getRobotPane().getChildren().clear();

        //server.communicateAll(new PlayerStatus(player.getID(), false));

        //lobbyController.getReadyCheckbox().setSelected(false);
        viewClient.sendMessage(new SetStatus(false));

        server.getReadyUsers().clear();
        server.setServerState(ServerState.LOBBY);
        viewManager.showLobby();


/*
        ArrayList<Controller> controllerList = new ArrayList<>();
        controllerList.add(gameController);
        //controllerList.add(gameBoardController);

        viewClient.setController(controllerList);

 */


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
