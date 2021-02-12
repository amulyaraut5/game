package client.view;

import game.Player;
import game.gameObjects.robot.Robot;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Coordinate;
import utilities.JSONProtocol.JSONBody;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.*;
import utilities.RegisterCard;
import utilities.SoundHandler;
import utilities.Updatable;
import utilities.enums.CardType;
import utilities.enums.GameState;
import utilities.enums.InnerActivation;
import utilities.enums.Rotation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static utilities.enums.CardType.*;

/**
 * The GameViewController class controls the GameView and coordinates its inner views.
 *
 * @author Simon
 * @author Sarah
 * @author Amulya
 */
public class GameController extends Controller implements Updatable {
    private static final Logger logger = LogManager.getLogger();

    /**
     * A list which contains all active players.
     */
    private final ArrayList<Player> activePlayers = new ArrayList<>();

    /**
     * The list stores the actions the player makes if he's the current player to display the
     * actions the damage card makes.
     */
    private final ArrayList<JSONBody> currentAction = new ArrayList<>();

    /**
     * The list stores all four possible damage CardTypes
     */
    private final ArrayList<CardType> damageCards = new ArrayList<>();

    /**
     * The controller of the PlayerMatController
     */
    private PlayerMatController playerMatController;
    /**
     * The controller of the ConstructionController
     */
    private ConstructionController constructionController;
    /**
     * The controller of the ProgrammingController
     */
    private ProgrammingController programmingController;
    /**
     * The controller of the ActivationController
     */
    private ActivationController activationController;
    /**
     * The controller of the OthersController
     */
    private OthersController othersController;
    /**
     * The controller of the GameWonController
     */
    private GameWonController gameWonController;
    /**
     * The controller of the GameBoardController
     */
    private GameBoardController gameBoardController;

    /**
     * It stores if the player was the first that selected all 5 registers.
     */
    private boolean allRegistersAsFirst = false;
    /**
     * It stores if the card the player has in current register is a damage card.
     */
    private boolean currentCardIsDamage = false;
    /**
     * The pane that stores the constructionPane.
     */
    private Pane constructionPane;
    /**
     * The pane that stores the programmingPane.
     */
    private Pane programmingPane;
    /**
     * The pane that stores the activationPane.
     */
    private Pane activationPane;
    /**
     * The pane that stores the gameWonPane.
     */
    private Pane gameWonPane;

    /**
     * The SoundHandler plays sound effects.
     */
    private SoundHandler soundHandler;
    /**
     * This GameState stores the currentPhase of the game.
     */
    private GameState currentPhase = GameState.CONSTRUCTION;
    /**
     * This stores the currentRound, it gets increased in the progress of the game.
     */
    private int currentRound = 1;
    /**
     * This stores if its the first round, then not so many attributes are reset.
     */
    private boolean first = true;
    /**
     * This stores if the game is muted.
     */
    private boolean isMuted = true;
    /**
     * This stores if the music is playing, the player can change ist by pressing a key.
     */
    private boolean play = false;

    /**
     * The pane shows information for the player.
     */
    @FXML
    private Pane infoPane;
    /**
     * The Label shows which moves the player does and that hos card were shuffled.
     */
    @FXML
    private Label moveInfo;
    /**
     * This Pane will store the onePlayerMats.
     */
    @FXML
    private Pane otherPlayerSpace;
    /**
     * The pane stores the playerMat.
     */
    @FXML
    private StackPane playerMat;
    /**
     * The pane stores the phasePane.
     */
    @FXML
    private BorderPane phasePane;
    /**
     * The pane stores the chatPane.
     */
    @FXML
    private BorderPane chatPane;
    /**
     * The label displays which round it currently is.
     */
    @FXML
    private Label roundLabel;
    /**
     * This pane contains the roundLabel and is invisible in the login, lobby or construction phase
     * and otherwise in activation- and programming phase it is visible.
     */
    @FXML
    private Pane roundPane;
    /**
     * The pane stores the boardPane.
     */
    @FXML
    private Pane boardPane;
    /**
     * This label displays the errors.
     */
    @FXML
    private Label infoLabel;

    /**
     * This method loads and adds the gameBoard, the playerMat and initializes the otherController view, it also
     * constructs the views of the different phases.
     */
    @FXML
    public void initialize() {
        soundHandler = new SoundHandler();
        roundPane.setVisible(false);
        constructPhaseViews();
        fillDamageCardsList();
        addGameBoard();
        addPlayerMat();
        addOtherPlayer();
    }

    /**
     * This method constructs the gameBoard, initializes the gameBoardController and adds it to gameView.
     */
    private void addGameBoard() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/innerViews/gameBoard.fxml"));
            boardPane.getChildren().add(fxmlLoader.load());
            gameBoardController = fxmlLoader.getController();
        } catch (IOException e) {
            logger.error("GameBoard could not be created: " + e.getMessage());
        }
    }

    /**
     * This method constructs the playerMat, initializes the playerMatController and adds it to gameView.
     */
    private void addPlayerMat() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/innerViews/playerMat.fxml"));
            playerMat.setAlignment(Pos.CENTER);
            playerMat.getChildren().add(fxmlLoader.load());
            playerMatController = fxmlLoader.getController();
        } catch (IOException e) {
            logger.error("PlayerMat could not be created: " + e.getMessage());
        }
    }

    /**
     * This method constructs the otherPlayer view , initializes the othersController and adds it to gameView.
     */
    private void addOtherPlayer() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/innerViews/otherPlayer.fxml"));
            otherPlayerSpace.getChildren().add(fxmlLoader.load());
            othersController = fxmlLoader.getController();
        } catch (IOException e) {
            logger.error("Other player mats could not be created: " + e.getMessage());
        }
    }

    /**
     * This method fills the damageCards list with all available damage CardTypes.
     */
    private void fillDamageCardsList() {
        damageCards.add(Spam);
        damageCards.add(Virus);
        damageCards.add(Trojan);
        damageCards.add(Worm);
    }

    /**
     * This method sets the chat in the chatPane with its width and height.
     *
     * @param chat the chatPane
     */
    public void attachChatPane(Pane chat) {
        chat.setPrefWidth(chatPane.getPrefWidth());
        chat.setPrefHeight(chatPane.getPrefHeight());
        chatPane.setCenter(chat);
    }

    /**
     * This method calls all controllers that need a reset if it's not the first round.
     */
    private void resetIfNotFirst() {
        if (!first) {
            playerMatController.reset();
            playerMatController.setDiscardDeckCounter(5);

            othersController.reset();
            activationController.reset();
        }
    }

    /**
     * This method resets everything at the start of a programming phase. it sets the nodes
     * visible or invisible and updates the current round in the label,
     * resets the deck counter and adds all the player to the activePlayers list.
     */
    private void resetInProgrammingPhase() {
        roundPane.setVisible(true);
        infoPane.setVisible(false);
        roundLabel.setText("Round " + currentRound);
        currentRound++;
        resetIfNotFirst();
        playerMatController.resetDeckCounter(9);

        activePlayers.addAll(viewClient.getPlayers());
        phasePane.setCenter(programmingPane);
        othersController.visibleHBoxRegister(true);
    }

    /**
     * This method changes the phases and reset elements related to the state of phases.
     *
     * @param phase which should be next
     */
    public void changePhaseView(GameState phase) {
        currentPhase = phase;

        switch (phase) {
            case CONSTRUCTION -> phasePane.setCenter(constructionPane);
            case PROGRAMMING -> resetInProgrammingPhase();
            case ACTIVATION -> {
                activationController.changePhaseView(InnerActivation.PlayIt);
                currentAction.clear();
                programmingController.reset();
                phasePane.setCenter(activationPane);
                othersController.visibleHBoxRegister(false);
                first = false;
            }
        }
    }

    /**
     * This method constructs the different phase views and their controllers.
     */
    private void constructPhaseViews() {
        FXMLLoader constructionLoader = new FXMLLoader(getClass().getResource("/view/innerViews/constructionView.fxml"));
        FXMLLoader programmingLoader = new FXMLLoader(getClass().getResource("/view/innerViews/programmingView.fxml"));
        FXMLLoader activationLoader = new FXMLLoader(getClass().getResource("/view/innerViews/activationView.fxml"));
        FXMLLoader gameWonLoader = new FXMLLoader(getClass().getResource("/view/innerViews/gameWonView.fxml"));

        try {
            constructionPane = constructionLoader.load();
            programmingPane = programmingLoader.load();
            activationPane = activationLoader.load();
            gameWonPane = gameWonLoader.load();

            constructionController = constructionLoader.getController();
            programmingController = programmingLoader.getController();
            activationController = activationLoader.getController();
            gameWonController = gameWonLoader.getController();

            activationController.changePhaseView(InnerActivation.PlayIt);
        } catch (IOException e) {
            logger.error("Inner phase View could not be loaded: " + e.getMessage());
        }
    }

    /**
     * This method checks the currentAction of the player, it then gets added to the moveInfo label,
     * which is only visible if the current card of the player is a damage card.
     *
     * @param currentAction a list with all actions since the player is the current player
     */
    private void setDisplayAction(ArrayList<JSONBody> currentAction) {
        StringBuilder text = new StringBuilder(" ");
        String defaultString = "You performed the card \non top of your deck";
        if (currentAction.size() == 1) {
            if (Energy.class.equals(currentAction.get(0).getClass())) {
                text.append("You got energy");
            } else if (Movement.class.equals(currentAction.get(0).getClass())) {
                text.append("You moved 1");
            } else if (PlayerTurning.class.equals(currentAction.get(0).getClass())) {
                text.append("You turned");
            } else text.append(defaultString);
        } else if (currentAction.size() == 2) {
            if (Movement.class.equals(currentAction.get(0).getClass()) && Movement.class.equals(currentAction.get(1).getClass())) {
                text.append("You moved 2");
            } else if (PlayerTurning.class.equals(currentAction.get(0).getClass()) && PlayerTurning.class.equals(currentAction.get(1).getClass())) {
                text.append("You performed an UTurn");
            } else text.append(defaultString);
        } else if (currentAction.size() == 3) {
            if (Movement.class.equals(currentAction.get(0).getClass()) && Movement.class.equals(currentAction.get(1).getClass()) && Movement.class.equals(currentAction.get(2).getClass())) {
                text.append("You moved 3");
            } else {
                text.append(defaultString);
            }
        }
        moveInfo.setText(String.valueOf(text));
    }

    /**
     * This method checks which JSONMessage the gameController got and handles it.
     *
     * @param message which the server sent which is related to the game
     */
    @Override
    public void update(JSONMessage message) {
        switch (message.getType()) {
            case Error -> {
                Error error = (Error) message.getBody();
                Updatable.showInfo(infoLabel, error.getError());
            }
            case GameStarted -> {
                GameStarted gameStarted = (GameStarted) message.getBody();
                gameBoardController.buildMap(gameStarted);
                othersController.createPlayerMats(viewClient.getPlayers());
            }
            case StartingPointTaken -> {
                StartingPointTaken msg = (StartingPointTaken) message.getBody();
                gameBoardController.placeRobotInMap(viewClient.getPlayerFromID(msg.getPlayerID()), Coordinate.parse(msg.getPosition()));
            }
            case ActivePhase -> {
                ActivePhase activePhase = (ActivePhase) message.getBody();
                changePhaseView(activePhase.getPhase());
            }
            case YourCards -> {
                YourCards yourCards = (YourCards) message.getBody();
                programmingController.startProgrammingPhase(yourCards.getCards());
            }
            case CardsYouGotNow -> {
                CardsYouGotNow cardsYouGotNow = (CardsYouGotNow) message.getBody();
                playerMatController.setNewCardsYouGotNow(cardsYouGotNow);
            }
            case Movement -> {
                Movement msg = (Movement) message.getBody();
                currentAction.add(msg);
                Player player = viewClient.getPlayerFromID(msg.getPlayerID());
                Coordinate newPos = Coordinate.parse(msg.getTo());
                player.getRobot().setCoordinate(newPos);
                gameBoardController.handleMovement(player, newPos);
            }
            case PlayerTurning -> {
                PlayerTurning msg = (PlayerTurning) message.getBody();
                currentAction.add(msg);
                Player player = viewClient.getPlayerFromID(msg.getPlayerID());

                Robot r = player.getRobot();
                int angle;
                if (msg.getDirection() == Rotation.LEFT) {
                    angle = -90;
                    r.setOrientation(r.getOrientation().getPrevious());
                } else {
                    angle = 90;
                    r.setOrientation(r.getOrientation().getNext());
                }
                gameBoardController.handlePlayerTurning(player, angle);
            }
            case CardSelected -> {
                CardSelected cardSelected = (CardSelected) message.getBody();
                othersController.getOtherPlayerController(cardSelected.getPlayerID()).cardSelected(cardSelected.getRegister());
            }
            case NotYourCards -> {
                NotYourCards notYourCards = (NotYourCards) message.getBody();
                othersController.setNotYourCards(notYourCards);
            }
            case PickDamage -> {
                PickDamage pickDamage = (PickDamage) message.getBody();
                activationController.changePhaseView(InnerActivation.Damage);
                activationController.getPickDamageController().pickDamage(pickDamage);
            }
            case PlayerShooting -> {
                if (activePlayers.isEmpty()) {
                    logger.info("No active players");
                } else {
                    soundHandler.playSoundEffects("Laser", play);
                    //gameBoardController.handleShooting(activePlayers);
                    //gameBoardController.handleRobotShooting(activePlayers);
                    gameBoardController.robotLaserAnimation(activePlayers);
                    gameBoardController.boardLaserAnimation(activePlayers);
                }
            }
            case Reboot -> {
                Reboot reboot = (Reboot) message.getBody();
                Player player = viewClient.getPlayerFromID(reboot.getPlayerID());
                activePlayers.remove(player);
                soundHandler.playSoundEffects("PitSound", play);

                boolean isThisPlayer = reboot.getPlayerID() == viewClient.getThisPlayersID();
                othersController.setRebootLabel(reboot, isThisPlayer);
            }
            case SelectionFinished -> {
                SelectionFinished selectionFinished = (SelectionFinished) message.getBody();
                if (selectionFinished.getPlayerID() == viewClient.getThisPlayersID()) {
                    playerMatController.fixSelectedCards();
                    allRegistersAsFirst = true;
                } else {
                    othersController.playerWasFirst(selectionFinished);
                    allRegistersAsFirst = false;
                }
            }
            case TimerStarted -> programmingController.startTimer(allRegistersAsFirst);
            case TimerEnded -> {
                TimerEnded timerEnded = (TimerEnded) message.getBody();
                programmingController.setTimerEnded(true);
                playerMatController.setDiscardDeckCounter(4);
                if (!allRegistersAsFirst) playerMatController.fixSelectedCards();
                allRegistersAsFirst = false; //TODO everything that is round related

                othersController.setTooSlowLabel(timerEnded);
            }
            case CurrentCards -> {
                CurrentCards currentCards = (CurrentCards) message.getBody();
                ArrayList<RegisterCard> otherPlayer = new ArrayList<>();
                for (RegisterCard registerCard : currentCards.getActiveCards()) {
                    if (registerCard.getPlayerID() == viewClient.getThisPlayersID()) {
                        activationController.currentCards(registerCard.getCard());
                    } else otherPlayer.add(registerCard);
                }
                othersController.currentCards(otherPlayer);

                for (int i = 0; i < currentCards.getActiveCards().size(); i++) {
                    if (currentCards.getActiveCards().get(i).getPlayerID() == viewClient.getThisPlayersID()) {
                        if (damageCards.contains(currentCards.getActiveCards().get(i).getCard())) {
                            playerMatController.subtractPlayerCards(1);
                            currentCardIsDamage = true;
                        } else {
                            currentCardIsDamage = false;
                        }
                    }
                }
            }
            case CurrentPlayer -> {
                CurrentPlayer currentPlayer = (CurrentPlayer) message.getBody();
                boolean isThisPlayer = currentPlayer.getPlayerID() == viewClient.getThisPlayersID();

                if (currentPhase == GameState.CONSTRUCTION) {

                    constructionController.currentPlayer(isThisPlayer);
                } else if (currentPhase == GameState.ACTIVATION) {
                    if (currentCardIsDamage) {
                        setDisplayAction(currentAction);
                        displayingTime(infoPane);
                    }
                    currentAction.clear();
                    Platform.runLater(() -> activationController.getPlayCardController().currentPlayer(isThisPlayer));

                    othersController.setInfoLabel(currentPlayer, isThisPlayer);
                }
            }
            case Energy -> {
                Energy energy = (Energy) message.getBody();
                if (energy.getPlayerID() == viewClient.getThisPlayersID()) {
                    playerMatController.addEnergy(energy.getCount());
                    currentAction.add(energy);
                } else {
                    othersController.addEnergy(energy);
                }
            }
            case CheckpointReached -> {
                CheckpointReached checkpointsReached = (CheckpointReached) message.getBody();
                if (checkpointsReached.getPlayerID() == viewClient.getThisPlayersID()) {
                    playerMatController.checkPointReached(checkpointsReached.getNumber());
                } else {
                    othersController.checkPointReached(checkpointsReached);
                }
                soundHandler.playSoundEffects("CheckPoint", play);
            }
            case ShuffleCoding -> {
                ShuffleCoding shuffleCoding = (ShuffleCoding) message.getBody();
                if (shuffleCoding.getPlayerID() == viewClient.getThisPlayersID()) {
                    infoPane.setVisible(true);
                    Platform.runLater(() -> moveInfo.setText("You're cards are shuffled"));
                    Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (moveInfo.getText().equals("You're cards are shuffled")) {
                                Platform.runLater(() -> infoPane.setVisible(false));
                            }
                            t.cancel();
                        }
                    }, 5000);
                } else {
                    othersController.setShuffleCodingLabel(shuffleCoding);
                }
            }
            case DiscardHand -> {
                DiscardHand discardHand = (DiscardHand) message.getBody();
                if (discardHand.getPlayerID() == viewClient.getThisPlayersID()) {
                    playerMatController.setDiscardDeckCounter(5);
                    playerMatController.resetDeckCounter(5);
                }
            }
            case DrawDamage -> {
                DrawDamage drawDamage = (DrawDamage) message.getBody();
                boolean isThisPlayer = drawDamage.getPlayerID() == viewClient.getThisPlayersID();
                viewClient.handleDamageCount(drawDamage.getCards());
                if (isThisPlayer) {
                    activationController.getPlayCardController().setDrawDamage(drawDamage);
                    playerMatController.setDiscardDeckCounter(drawDamage.getCards().size());
                    playerMatController.addPlayerCards(drawDamage.getCards().size());
                }
                othersController.setDrewDamageLabel(drawDamage, isThisPlayer);
            }
            case GameWon -> {
                GameWon gameWon = (GameWon) message.getBody();
                phasePane.setCenter(gameWonPane);
                gameWonController.setWinnerLabel(viewClient.getPlayerFromID(gameWon.getPlayerID()));
                soundHandler.playSoundEffects("Victory", play);
            }
        }
    }

    /**
     * This method handles what should happen if the player presses the keys W, D, S, A, M, P.
     * Each of those hotkeys has impacts.
     *
     * <p> W, A, S, D for manipulating the orientation of the robot</p>
     * <p> M, P for manipulating the music</p>
     *
     * @param event which key gets pressed
     */
    public void keyPressed(KeyEvent event) {
        String orientation = "";
        switch (event.getCode()) {
            case W -> orientation = "u";
            case D -> orientation = "r";
            case S -> orientation = "d";
            case A -> orientation = "l";
            case M -> {
                if (isMuted) {
                    isMuted = false;
                    soundHandler.musicOn();
                } else {
                    isMuted = true;
                    soundHandler.musicOff();
                }
            }
            case P -> play = !play;
        }
        if (!orientation.equals("")) {
            viewClient.sendMessage(new SendChat("#r " + orientation, -1));
        }
    }

    /**
     * This method removes a player by exiting from the othersController and its small playerMat and
     * also it removes the player from the gameBoard.
     *
     * @param player which should get removed
     */
    public void removePlayer(Player player) {
        gameBoardController.removePlayer(player);
        if (viewClient.getCurrentController().equals(this)) othersController.removePlayer(player);
    }

    /**
     * TODO
     */
    public void resetFocus() {
        boardPane.requestFocus();
    }

    /**
     * This method returns the playerMatController.
     *
     * @return the playerMatController
     */
    public PlayerMatController getPlayerMatController() {
        return playerMatController;
    }

    public Pane getBoardPane() {
        return boardPane;
    }

    public GameBoardController getGameBoardController() {
        return gameBoardController;
    }

    public OthersController getOthersController() {
        return othersController;
    }


}