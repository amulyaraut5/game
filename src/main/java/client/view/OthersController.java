package client.view;

import game.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.body.*;
import utilities.RegisterCard;

import java.io.IOException;
import java.util.ArrayList;

/**
 * OthersController handles the HBox underneath the gamemat and creates as much little playermats (onePlayer.fxml)
 * as other player that joined and handles incoming protocol messages.
 * @author sarah
 */
public class OthersController extends Controller {
    private static final Logger logger = LogManager.getLogger();
    @FXML
    private HBox hBoxPlayer;
    private ArrayList<OtherPlayer> otherPlayers = new ArrayList<>();

    /**
     * This method resets every one player mat that was created
     */
    public void reset(){
        for(OtherPlayer otherPlayer: otherPlayers) otherPlayer.getOnePlayerController().reset();
    }

    /**
     * This method creates for every player except player itseelf a little playermat
     * @param players
     */
    public  void createPlayerMats(ArrayList<Player> players){
        hBoxPlayer.setAlignment(Pos.CENTER);
        for (Player player: players){
            if(player.getID() != client.getThisPlayersID()){
                playerAdded(player);
            }
        }
    }

    private void playerAdded(Player player){
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
     * This method removes the little player mat of an other player when he exits the game
     * @param player who gets removed
     */
    public void removePlayer(Player player){
        OtherPlayer removedPlayer = getOtherPlayer(player.getID());
        hBoxPlayer.getChildren().remove(removedPlayer.getPositionHBox());
        otherPlayers.remove(getOtherPlayer(player.getID()));
    }

    /**
     * This method extracts the OtherPlayer with its controller etc
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

    /**
     * returns the OnePlayerController of the given id
     * @param id of wanted player
     * @return OnePlayerController of player
     */
    public OnePlayerController getOtherPlayerController(int id){
        return getOtherPlayer(id).getOnePlayerController();
    }

    /**
     * This method determines the visibility of the registers of each one player mat
     * @param visible boolean if hBox of registers should be visible
     */
    public void visibleHBoxRegister(boolean visible){
        for (OtherPlayer otherPlayer : otherPlayers){
            otherPlayer.getOnePlayerController().setHBoxRegisterVisible(visible);
        }
    }

    /**
     *
     * @param currentCards
     */
    public void currentCards(ArrayList<RegisterCard> currentCards){
        for(RegisterCard registerCard : currentCards){
             getOtherPlayerController(registerCard.getPlayerID()).currentCard(registerCard.getCard());
        }
    }

    /**
     *
     * @param energy
     */
    public void addEnergy(Energy energy){
        getOtherPlayerController(energy.getPlayerID()).addEnergy(energy.getCount());
    }

    /**
     *
     * @param selectionFinished
     */
    public void playerWasFirst(SelectionFinished selectionFinished){
        getOtherPlayerController(selectionFinished.getPlayerID()).setInfoLabel("This player was first");
    }

    /**
     *
     * @param checkpointReached
     */
    public void checkPointReached(CheckpointReached checkpointReached){
        getOtherPlayerController(checkpointReached.getPlayerID()).addCheckPoint(checkpointReached.getNumber());
    }

    /**
     *
     * @param currentPlayer
     * @param thisPlayer
     */
    public void setInfoLabel(CurrentPlayer currentPlayer, boolean thisPlayer){
        if(thisPlayer)
            for(OtherPlayer otherPlayer: otherPlayers) otherPlayer.getOnePlayerController().setInfoLabel(" ");
        else {
            for(OtherPlayer otherPlayer: otherPlayers){
                if(otherPlayer.getPlayer().getID() == currentPlayer.getPlayerID()){
                    getOtherPlayerController(otherPlayer.getPlayer().getID()).setInfoLabel("It's this players turn");
                } else {
                    getOtherPlayerController(otherPlayer.getPlayer().getID()).setInfoLabel(" ");
                }
            }
        }
    }

    /**
     *
     * @param notYourCards
     */
    public void setNotYourCards(NotYourCards notYourCards){
        getOtherPlayerController(notYourCards.getPlayerID()).setInfoLabel(notYourCards.getCards() + " programming cards");
    }

    private class OtherPlayer{
        private Player otherPlayer;
        private OnePlayerController onePlayerController;
        private int positionHBox;

        /**
         *
         * @param otherPlayer
         * @param onePlayerController
         * @param positionHBox
         */
        public OtherPlayer(Player otherPlayer, OnePlayerController onePlayerController, int positionHBox) {
            this.otherPlayer = otherPlayer;
            this.onePlayerController = onePlayerController;
            this.positionHBox = positionHBox;
        }

        /**
         *
         * @return
         */
        public Player getPlayer() {
            return otherPlayer;
        }

        /**
         *
         * @return
         */
        public OnePlayerController getOnePlayerController() {
            return onePlayerController;
        }

        /**
         *
         * @return
         */
        public int getPositionHBox() {
            return positionHBox;
        }


    }

}
