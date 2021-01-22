package client.model;

import client.ViewManager;
import client.view.*;
import game.Player;
import game.gameObjects.cards.programming.Again;
import game.gameObjects.cards.programming.MoveI;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Coordinate;
import utilities.JSONProtocol.JSONBody;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.Multiplex;
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.*;
import utilities.RegisterCard;
import utilities.Utilities;
import utilities.enums.CardType;
import utilities.enums.MessageType;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static utilities.Utilities.PORT;

/**
 * This Singleton Class handles the connection and disconnection to the server.
 * It also stores in the data structure {@code players} all information from other players
 * relevant for displaying them in the view and updates the ViewModel.
 *
 * @author sarah
 */
public class Client {
    private static final Logger logger = LogManager.getLogger();

    private static Client instance;
    private final ViewManager viewManager = ViewManager.getInstance();
    private final ArrayList<Player> players = new ArrayList<>();
    private int thisPlayersID;
    private PrintWriter writer;
    private boolean allRegistersAsFirst = false;

    private GameViewController gameViewController;
    private LoginController loginController;
    private LobbyController lobbyController;
    private ChatController chatController;


    /**
     * Stream socket which get connected to the specified port number on the named host of the server.
     */
    private Socket socket;
    /**
     * The readerThread reads the input of the user from given socket.
     */
    private ReaderThread readerThread;

    /**
     * the printWriter which writes messages onto the socket connected to the server.
     */

    public static Client getInstance() {
        if (instance == null) instance = new Client();
        return instance;
    }

    public int getThisPlayersID() {
        return thisPlayersID;
    }

    /**
     * This method establishes the connection between the server and the client using the assigned hostname and port.
     * If this was successful it creates a ReaderThread and a WriterThread which handle the communication onwards.
     */
    public boolean establishConnection() {
        try {
            String hostname = "localhost";
            socket = new Socket(hostname, PORT);
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

            readerThread = new ReaderThread(socket, this);
            readerThread.start();

            logger.info("Connection to server successful.");
            return true;
        } catch (IOException e) {
            logger.warn("No connection to server: " + e.getMessage());
        }
        return false;
    }

    /**
     * This method ends the client program. The reader and writer threads get interrupted and the socket is closed.
     */
    public void disconnect() {
        readerThread.interrupt();
        try {
            socket.close();
            logger.info("The connection with the server is closed.");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * This method should be called if a critical exception occurs in the client. (e.g. socket closed)
     * The reader and writer threads get interrupted and the socket is closed.
     *
     * @param ex The exception which occurred
     */
    public void disconnect(Exception ex) {
        readerThread.interrupt();
        logger.warn("The server is no longer reachable: " + ex.getMessage());
        try {
            socket.close();
            logger.info("The connection with the server is closed.");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

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
                case HelloClient -> sendMessage(new HelloServer(Utilities.PROTOCOL, "Astreine Akazien", false));
                case Welcome -> {
                    Welcome wc = (Welcome) message.getBody();
                    thisPlayersID = wc.getPlayerID();
                    viewManager.nextScene();
                }
                case PlayerAdded -> {
                    PlayerAdded playerAdded = (PlayerAdded) message.getBody();
                    addNewPlayer(playerAdded);
                }
                case Error -> {
                    Error error = (Error) message.getBody(); //TODO
                }
                case ConnectionUpdate -> {
                    ConnectionUpdate msg = (ConnectionUpdate) message.getBody();
                    if (msg.getAction().equals("Remove") && msg.isConnected() == false) {
                        Player player = getPlayerFromID(msg.getID());
                        loginController.removePlayer(player);
                        lobbyController.removePlayer(player);
                        gameViewController.removePlayer(player);
                        players.remove(player);

                    }
                }
                case PlayerStatus -> {
                    PlayerStatus playerStatus = (PlayerStatus) message.getBody();
                    lobbyController.displayStatus(playerStatus);
                }
                case ReceivedChat -> {
                    ReceivedChat receivedChat = (ReceivedChat) message.getBody();
                    chatController.receivedChat(receivedChat);
                }
                case GameStarted -> {
                    GameStarted gameStarted = (GameStarted) message.getBody();
                    gameViewController.buildMap(gameStarted);
                    viewManager.nextScene();
                }
                case StartingPointTaken -> {
                    StartingPointTaken msg = (StartingPointTaken) message.getBody();
                    gameViewController.placeRobotInMap(getPlayerFromID(msg.getPlayerID()), Coordinate.parse(msg.getPosition()));
                    gameViewController.handleShooting(players);
                }
                case ActivePhase -> {
                    ActivePhase activePhase = (ActivePhase) message.getBody();
                    gameViewController.changePhaseView(activePhase.getPhase());
                }
                case YourCards -> {
                    YourCards yourCards = (YourCards) message.getBody();
                    gameViewController.getProgrammingController().startProgrammingPhase(yourCards.getCards());
                }
                case CardsYouGotNow -> {
                    CardsYouGotNow cardsYouGotNow = (CardsYouGotNow) message.getBody();
                    gameViewController.getPlayerMapController().setNewCardsYouGotNow(cardsYouGotNow);
                }
                case SelectionFinished -> {
                    SelectionFinished selectionFinished = (SelectionFinished) message.getBody();
                    if (selectionFinished.getPlayerID() == thisPlayersID) {
                        gameViewController.getPlayerMapController().fixSelectedCards();
                        allRegistersAsFirst = true; //TODO reset after one round
                    } else {
                        gameViewController.getPlayerMapController().fixSelectedCards(); //TODO else
                        allRegistersAsFirst = false;
                    }

                }
                case TimerStarted -> {
                    gameViewController.getProgrammingController().startTimer(allRegistersAsFirst);
                }
                case TimerEnded -> {
                    gameViewController.getProgrammingController().setTimerEnded(true);
                }
                case CurrentCards -> {
                    //CurrentCards currentCards = (CurrentCards) message.getBody();
                    ArrayList<RegisterCard> registerCards = new ArrayList<>();
                    registerCards.add(new RegisterCard(thisPlayersID, new Again()));
                    registerCards.add(new RegisterCard(players.get(1).getID(), new MoveI()));
                    CurrentCards currentCards = new CurrentCards(registerCards);
                    //HARDCODED eigentlich kein Kartenobjekt hier !
                    gameViewController.getActivationController().currentCards(currentCards);

                }
                case Reboot -> {
                    Reboot reboot = (Reboot) message.getBody();
                    // TODO display the message
                }
                case Energy -> {
                    Energy energy = (Energy) message.getBody();
                }
                case CheckPointReached -> {
                    CheckpointReached checkpointsReached = (CheckpointReached) message.getBody();
                    //TODO display the message, update player mat
                }
                case GameWon -> {
                    GameWon gameWon = (GameWon) message.getBody();
                    //TODO display and end game
                }
                case Movement -> {
                    Movement msg = (Movement) message.getBody();
                    gameViewController.handleMovement(getPlayerFromID(msg.getPlayerID()), Coordinate.parse(msg.getTo()));
                }
                case PlayerTurning -> {
                    PlayerTurning pT = (PlayerTurning) message.getBody();
                    gameViewController.handlePlayerTurning(getPlayerFromID(pT.getPlayerID()), pT.getDirection());

                }
                default -> logger.error("The MessageType " + type + " is invalid or not yet implemented!");
            }
        });
    }

    public void addNewPlayer(PlayerAdded playerAdded) {
        Player player = new Player(playerAdded);
        players.add(player);

        if (thisPlayersID == player.getID()) {
            gameViewController.getPlayerMapController().loadPlayerMap(player);
            viewManager.nextScene();
        }
        loginController.setFigureTaken(player.getFigure(), true);
        lobbyController.addJoinedPlayer(player, false);
        chatController.addUser(player);
    }

    /**
     * Sends a JSONMessage to the server. The JSONMessage is serialized therefore.
     *
     * @param jsonBody Body of the JSONMessage which should be sent.
     */
    public void sendMessage(JSONBody jsonBody) {
        String json = Multiplex.serialize(JSONMessage.build(jsonBody));
        logger.debug("Protocol sent: " + json);
        writer.println(json);
        writer.flush();
    }

    /**
     * Gets a player based on their ID from the list of players saved in {@link Client}.
     *
     * @param id ID of the wanted player.
     * @return Unique player with the ID, {@code null} if no player with the ID exists.
     */
    public Player getPlayerFromID(int id) {
        for (Player player : players) {
            if (player.getID() == id) return player;
        }
        return null;
    }

    public void setController(ArrayList<Controller> controllerList) {
        loginController = (LoginController) controllerList.get(0);
        lobbyController = (LobbyController) controllerList.get(1);
        gameViewController = (GameViewController) controllerList.get(2);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
    }
}