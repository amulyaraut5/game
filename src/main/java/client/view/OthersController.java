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
import java.util.Objects;

/**
 * OthersController handles the HBox underneath the gameMat and creates as much little playerMats (onePlayer.fxml)
 * as other player that joined and handles incoming protocol messages.
 *
 * @author sarah
 */
public class OthersController extends Controller {
    private static final Logger logger = LogManager.getLogger();
    /**
     * This method stores all players of the game except the player itself.
     */
    private final ArrayList<OtherPlayer> otherPlayers = new ArrayList<>();
    /**
     * This HBox contains all small onePlayerMats.
     */
    @FXML
    private HBox hBoxPlayer;

    /**
     * This method resets every one player mat that was created after one round
     */
    public void reset() {
        for (OtherPlayer otherPlayer : otherPlayers) otherPlayer.getOnePlayerController().reset();
    }

    /**
     * This method extracts the OtherPlayer with its controller etc.
     *
     * @param id of searched player
     * @return the player who is desired
     */
    private OtherPlayer getOtherPlayer(int id) {
        for (OtherPlayer otherPlayer : otherPlayers) {
            if (id == otherPlayer.getPlayer().getID()) {
                return otherPlayer;
            }
        }
        return null;
    }

    /**
     * This method returns the OnePlayerController of the given id.
     *
     * @param id of wanted player
     * @return OnePlayerController of player
     */
    public OnePlayerController getOtherPlayerController(int id) {
        return Objects.requireNonNull(getOtherPlayer(id)).getOnePlayerController();
    }

    /**
     * This method creates for every player except player itself a little playerMat.
     *
     * @param players a list with all players
     */
    public void createPlayerMats(ArrayList<Player> players) {
        hBoxPlayer.setAlignment(Pos.CENTER);
        for (Player player : players) {
            if (player.getID() != viewClient.getThisPlayersID()) {
                playerAdded(player);
            }
        }
    }

    /**
     * This method creates the innerView of one player mat and adds it to the hBoxPlayer.
     * Also it creates and adds an instance of the private class OtherPlayer and assigns the position in HBox.
     *
     * @param player the player that gets added
     */
    private void playerAdded(Player player) {
        hBoxPlayer.setSpacing(10);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/innerViews/onePlayer.fxml"));
            fxmlLoader.load();
            hBoxPlayer.getChildren().add(fxmlLoader.getRoot());
            OnePlayerController onePlayerController = fxmlLoader.getController();
            int position = hBoxPlayer.getChildren().indexOf(fxmlLoader.getRoot());
            otherPlayers.add(new OtherPlayer(player, onePlayerController, position));
            onePlayerController.setPlayerInformation(player);
        } catch (IOException e) {
            logger.error("OnePlayer mats can not be created: " + e.getMessage());
        }
    }

    /**
     * This method removes the little player mat of an other player when he exits the game
     *
     * @param player who gets removed
     */
    public void removePlayer(Player player) {
        OtherPlayer removedPlayer = getOtherPlayer(player.getID());
        assert removedPlayer != null;
        hBoxPlayer.getChildren().remove(removedPlayer.getPositionHBox());
        otherPlayers.remove(getOtherPlayer(player.getID()));
    }

    /**
     * This method determines the visibility of the registers of each one player mat
     *
     * @param visible boolean if hBox of registers should be visible
     */
    public void visibleHBoxRegister(boolean visible) {
        for (OtherPlayer otherPlayer : otherPlayers) {
            otherPlayer.getOnePlayerController().setHBoxRegisterVisible(visible);
        }
    }

    /**
     * This method shows for every player the currentCard of the current register on its playerMat
     *
     * @param currentCards this list contains all information about
     */
    public void currentCards(ArrayList<RegisterCard> currentCards) {
        for (RegisterCard registerCard : currentCards) {
            getOtherPlayerController(registerCard.getPlayerID()).currentCard(registerCard.getCard());
        }
    }

    /**
     * This method adds to the playerMat of the player that got energy a new amount of energy
     *
     * @param energy the playerID and the amount of energy the player got
     */
    public void addEnergy(Energy energy) {
        getOtherPlayerController(energy.getPlayerID()).addEnergy(energy.getCount());
    }

    /**
     * This method sets the infoLabel of the other player that was first, so that the player can recognize who it was.
     *
     * @param selectionFinished the player that was first
     */
    public void playerWasFirst(SelectionFinished selectionFinished) {
        getOtherPlayerController(selectionFinished.getPlayerID()).setInfoLabel("This player was first");
    }

    /**
     * This method adds to the playerMat of the player that got a new checkpoint a the checkpoint number
     *
     * @param checkpointReached the playerID and the number of checkpoint of a player that reached a new checkpoint
     */
    public void checkPointReached(CheckpointReached checkpointReached) {
        getOtherPlayerController(checkpointReached.getPlayerID()).addCheckPoint(checkpointReached.getNumber());
    }

    /**
     * This method displays the reboot and spam information in the small player mat.
     *
     * @param reboot the playerID of the rebooted player
     */
    public void setRebootLabel(Reboot reboot) {
            getOtherPlayerController(reboot.getPlayerID()).setDisplayingLabel("rebooted and got spam!");
    }

    /**
     * This method displays on their playerMats which player was too slow.
     *
     * @param timerEnded the player who didn't finish filling their registers in time
     */
    public void setTooSlowLabel(TimerEnded timerEnded) {
        for (int playerID : timerEnded.getPlayerIDs()) {
            if (playerID != viewClient.getThisPlayersID())
                getOtherPlayerController(playerID).setDisplayingLabel("programmed too slowly!");
        }
    }

    /**
     * This method displays the information (including images) about damage on the
     * small playerMat of the player that got damage.
     *
     * @param drawDamage The playerID of the player that got damage and which cards
     */
    public void setDrewDamageLabel(DrawDamage drawDamage) {
        for (OtherPlayer otherPlayer : otherPlayers) otherPlayer.getOnePlayerController().setInfoLabel(" ");
        getOtherPlayerController(drawDamage.getPlayerID()).setDisplayingLabel("got damage");
        getOtherPlayerController(drawDamage.getPlayerID()).displayDamageCards(drawDamage.getCards());
    }

    /**
     * This method displays the information shuffling cards on the
     * small playerMat of the player that shuffled cards.
     *
     * @param shuffleCoding The playerID of the player that shuffled cards
     */
    public void setShuffleCodingLabel(ShuffleCoding shuffleCoding) {
        getOtherPlayerController(shuffleCoding.getPlayerID()).setDisplayingLabel("refilled the deck");
    }

    /**
     * This method sets the information label cards on the
     * small playerMat of the player that shuffled cards
     *
     * @param currentPlayer
     * @param thisPlayer
     */
    public void setInfoLabel(CurrentPlayer currentPlayer, boolean thisPlayer) {
        if (thisPlayer)
            for (OtherPlayer otherPlayer : otherPlayers) otherPlayer.getOnePlayerController().setInfoLabel(" ");
        else {
            for (OtherPlayer otherPlayer : otherPlayers) {
                if (otherPlayer.getPlayer().getID() == currentPlayer.getPlayerID()) {
                    getOtherPlayerController(otherPlayer.getPlayer().getID()).setInfoLabel("It's this players turn:");
                } else {
                    getOtherPlayerController(otherPlayer.getPlayer().getID()).setInfoLabel(" ");
                }
            }
        }
    }

    /**
     * @param notYourCards
     */
    public void setNotYourCards(NotYourCards notYourCards) {
        getOtherPlayerController(notYourCards.getPlayerID()).setInfoLabel(notYourCards.getCardsInHand() + " programming cards");
    }

    private static class OtherPlayer {
        private final Player otherPlayer;
        private final OnePlayerController onePlayerController;
        private final int positionHBox;

        /**
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
         * @return
         */
        public Player getPlayer() {
            return otherPlayer;
        }

        /**
         * @return
         */
        public OnePlayerController getOnePlayerController() {
            return onePlayerController;
        }

        /**
         * @return
         */
        public int getPositionHBox() {
            return positionHBox;
        }
    }

}
