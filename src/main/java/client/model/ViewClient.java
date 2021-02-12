package client.model;

import client.ViewManager;
import client.view.*;
import game.Player;
import javafx.application.Platform;
import utilities.Constants;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.*;
import utilities.Updatable;
import utilities.enums.MessageType;
import java.util.ArrayList;

/**
 * This Singleton Class handles the connection and disconnection to the server.
 * It also stores in the data structure {@code players} all information from other players
 * relevant for displaying them in the view and updates the ViewModel.
 *
 * @author sarah
 */
public class ViewClient extends Client {
    /**
     * The instance of the ViewClient.
     */
    private static ViewClient instance;
    /**
     * The ViewManager which handles the different views, scenes and windows.
     */
    private final ViewManager viewManager = ViewManager.getInstance();
    /**
     * The GameController which gets used during the actual game.
     */
    private GameController gameController;
    /**
     * The LoginLogger gets used for the loginView
     */
    private LoginController loginController;
    /**
     * The LobbyController gets used for the lobbyView
     */
    private LobbyController lobbyController;
    /**
     * The ChatController gets used for the chatView
     */
    private ChatController chatController;

    /**
     * The currentController of the currently open view
     */
    private Updatable currentController;


    /**
     * Based on the messageType the various protocol are differentiated and Object class type
     * is casted down to respective class and then it interacts with the different controllers.
     *
     * @param message received JSONMessage from the server which needs to be handled
     */
    public void handleMessage(JSONMessage message) {
        MessageType type = message.getType();

        Platform.runLater(() -> {
            switch (type) {
                case SelectMap, Reboot, Error, StartingPointTaken, YourCards, Movement,
                        PlayerTurning, CardSelected, NotYourCards, PickDamage, PlayerShooting,
                        ActivePhase, CardsYouGotNow, SelectionFinished, TimerStarted, TimerEnded,
                        CurrentCards, CurrentPlayer, Energy, CheckpointReached, ShuffleCoding,
                        DiscardHand, SelectDamage, DrawDamage, GameWon -> currentController.update(message);
                case HelloClient -> sendMessage(new HelloServer(Constants.PROTOCOL, "Astreine Akazien", false));
                case Welcome -> {
                    Welcome wc = (Welcome) message.getBody();
                    thisPlayersID = wc.getPlayerID();
                    viewManager.showLogin();
                }
                case PlayerAdded -> {
                    PlayerAdded playerAdded = (PlayerAdded) message.getBody();
                    addNewPlayer(playerAdded);
                }
                case PlayerStatus, MapSelected -> lobbyController.update(message);
                case ConnectionUpdate -> {
                    ConnectionUpdate msg = (ConnectionUpdate) message.getBody();
                    if (msg.getAction().equals("Remove") && !msg.isConnected()) {
                        Player player = getPlayerFromID(msg.getID());
                        loginController.removePlayer(player);
                        lobbyController.removePlayer(player);
                        gameController.removePlayer(player);
                        players.remove(player);

                        if (players.size() <= 1) {
                            System.exit(0);
                        }
                    }
                }
                case ReceivedChat -> {
                    ReceivedChat receivedChat = (ReceivedChat) message.getBody();
                    chatController.receivedChat(receivedChat);
                }
                case GameStarted -> {
                    viewManager.showGame();
                    currentController.update(message);
                }
                default -> logger.error("The MessageType " + type + " is invalid or not yet implemented!");
            }
        });
    }

    /**
     * This method adds a new player with its name, shows the related components in the view and shows
     * which figure is now taken.
     *
     * @param playerAdded a joining player
     */
    public void addNewPlayer(PlayerAdded playerAdded) {
        Player player = new Player(playerAdded);
        players.add(player);

        if (thisPlayersID == player.getID()) {
            gameController.getPlayerMatController().loadPlayerMap(player);
            viewManager.showLobby();
        }
        loginController.setFigureTaken(player.getID(), player.getFigure());
        lobbyController.addJoinedPlayer(player);
        chatController.addUser(player);
    }

    /**
     * This method sets different controllers and gets called from the ViewManager
     *
     * @param controllerList a list with all required controllers
     */
    public void setController(ArrayList<Controller> controllerList) {
        loginController = (LoginController) controllerList.get(0);
        lobbyController = (LobbyController) controllerList.get(1);
        gameController = (GameController) controllerList.get(2);
    }

    /**
     * This method returns the instance of this Singleton class
     *
     * @return the instance
     */
    public static ViewClient getInstance() {
        if (instance == null) instance = new ViewClient();
        return instance;
    }


    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
    }

    public Updatable getCurrentController() {
        return currentController;
    }

    public void setCurrentController(Updatable currentController) {
        this.currentController = currentController;
    }
}