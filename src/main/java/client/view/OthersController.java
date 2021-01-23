package client.view;

import game.Player;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.body.*;

import java.io.IOException;
import java.util.ArrayList;

public class OthersController extends Controller {
    private static final Logger logger = LogManager.getLogger();
    public HBox hBoxPlayer;

    private ArrayList<OtherPlayer> otherPlayers = new ArrayList<>();

    public  void createPlayerMats(ArrayList<Player> players){
        hBoxPlayer.setAlignment(Pos.CENTER);
        for (Player player: players){
            if(player.getID() != client.getThisPlayersID()){
                playerAdded(player);
            }
        }
    }

    public void playerAdded(Player player){

        hBoxPlayer.setSpacing(20);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/innerViews/onePlayer.fxml"));
            fxmlLoader.load();
            hBoxPlayer.getChildren().add(fxmlLoader.getRoot());
            OnePlayerController onePlayerController  = fxmlLoader.getController();
            int position = hBoxPlayer.getChildren().indexOf(fxmlLoader.getRoot());
            otherPlayers.add(new OtherPlayer(player, onePlayerController, position));
            onePlayerController.setPlayerInformation(player);

        } catch (IOException e) {
            logger.error("OnePlayer mats can not be created: " + e.getMessage());
        }
    }


    /**
     * extracts the OtherPlayer with its controller etc
     * @param id of searched player
     * @return the player who is desired
     */
    private OtherPlayer getOtherPlayer(int id){
        for (OtherPlayer otherPlayer : otherPlayers){
            if(id == otherPlayer.getPlayer().getID()){
                return otherPlayer;
            }
        }
        return null;
    }
    public OnePlayerController getOtherPlayerController(int id){
        for (OtherPlayer otherPlayer : otherPlayers){
            if(id == otherPlayer.getPlayer().getID()){
                return otherPlayer.getOnePlayerController();
            }
        }
        return null;
    }


    public void addEnergy(Energy energy){
        getOtherPlayer(energy.getPlayerID()).getOnePlayerController().addEnergy(energy.getCount());
    }

    public void playerWasFirst(SelectionFinished selectionFinished){
        getOtherPlayer(selectionFinished.getPlayerID()).getOnePlayerController().setInfoLabel("This player was first");
    }
    public void checkPointReached(CheckpointReached checkpointReached){
        getOtherPlayer(checkpointReached.getPlayerID()).getOnePlayerController().addCheckPoint(checkpointReached.getNumber());
    }

    private class OtherPlayer{
        private Player otherPlayer;
        private OnePlayerController onePlayerController;
        private int positionHBox;

        public OtherPlayer(Player otherPlayer, OnePlayerController onePlayerController, int positionHBox) {
            this.otherPlayer = otherPlayer;
            this.onePlayerController = onePlayerController;
            this.positionHBox = positionHBox;
        }

        public Player getPlayer() {
            return otherPlayer;
        }

        public OnePlayerController getOnePlayerController() {
            return onePlayerController;
        }

        public int getPositionHBox() {
            return positionHBox;
        }
    }

}
