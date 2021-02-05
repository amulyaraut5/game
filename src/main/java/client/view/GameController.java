package client.view;

import game.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static utilities.enums.CardType.*;

/**
 * The GameViewController class controls the GameView and coordinates its inner views
 *
 * @author Simon
 * @author Sarah
 * @author Amulya
 */
public class GameController extends Controller implements Updatable {
    private static final Logger logger = LogManager.getLogger();

    private final ArrayList<Player> activePlayers = new ArrayList<>();
    private final ArrayList<JSONBody> currentAction = new ArrayList<>();
    private PlayerMatController playerMatController;
    private ConstructionController constructionController;
    private ProgrammingController programmingController;
    private ActivationController activationController;
    private OthersController othersController;
    private GameWonController gameWonController;
    private GameBoardController gameBoardController;

    private boolean allRegistersAsFirst = false;
    private Pane constructionPane;
    private Pane programmingPane;
    private Pane activationPane;
    private Pane gameWonPane;

    private SoundHandler soundHandler;

    private GameState currentPhase = GameState.CONSTRUCTION;
    private int currentRound = 1;
    private boolean first = true;
    private boolean isMuted = true;
    private boolean play = false;

    public Pane infoPane;
    @FXML
    private  Label moveInfo;
    @FXML
    private HBox otherPlayerSpace;
    @FXML
    private StackPane playerMat;
    @FXML
    private BorderPane phasePane;
    @FXML
    private BorderPane chatPane;
    @FXML
    private Label roundLabel;
    @FXML
    private Pane roundPane;
    @FXML
    private Pane boardPane;
    @FXML
    private Label infoLabel;
    @FXML
    private AnchorPane drawDamageAnchorPane;
    @FXML
    private Label drawDamageLabel;
    @FXML
    private HBox drawDamageHBox;

    @FXML
    public void initialize() {
        drawDamageHBox.setAlignment(Pos.CENTER);
        drawDamageHBox.setSpacing(5);
        drawDamageAnchorPane.setVisible(false);
        constructPhaseViews();
        roundPane.setVisible(false);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/innerViews/gameBoard.fxml"));
            boardPane.getChildren().add(fxmlLoader.load());

            gameBoardController = fxmlLoader.getController();
        } catch (IOException e) {
            logger.error("PlayerMap could not be created: " + e.getMessage());
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/innerViews/playerMat.fxml"));
            playerMat.setAlignment(Pos.CENTER);
            playerMat.getChildren().add(fxmlLoader.load());

            playerMatController = fxmlLoader.getController();
        } catch (IOException e) {
            logger.error("PlayerMap could not be created: " + e.getMessage());
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/innerViews/otherPlayer.fxml"));
            otherPlayerSpace.setSpacing(20);
            otherPlayerSpace.getChildren().add(fxmlLoader.load());
            othersController = fxmlLoader.getController();
        } catch (IOException e) {
            logger.error("Other player maps could not be created: " + e.getMessage());
        }

        soundHandler = new SoundHandler();
    }

    public void attachChatPane(Pane chat) {
        chat.setPrefWidth(chatPane.getPrefWidth());
        chat.setPrefHeight(chatPane.getPrefHeight());
        chatPane.setCenter(chat);
    }

    public void setDrawDamage(DrawDamage drawDamage) {
        drawDamageHBox.getChildren().clear();

        drawDamageLabel.setText("You got damage!");
        for (int i = 0; i < drawDamage.getCards().size(); i++) {
            String path = "/cards/programming/" + drawDamage.getCards().get(i).name() + "-card.png";
            ImageView damage = new ImageView(new Image(getClass().getResource(path).toString()));
            damage.setFitWidth(30);
            damage.setFitHeight(50);
            drawDamageHBox.getChildren().add(damage);
        }

        Platform.runLater(() -> drawDamageAnchorPane.setVisible(true));
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if (drawDamageAnchorPane.isVisible()) {
                    Platform.runLater(() -> drawDamageAnchorPane.setVisible(false));
                }
                t.cancel();
            }
        }, 2000);
    }

    private void resetIfNotFirst(){
        if (!first) {
            playerMatController.reset();
            playerMatController.setDiscardDeckCounter(5);

            othersController.reset();
            activationController.reset();
        }
    }

    private void resetInProgrammingPhase(){
        //playerMatController.fixSelectedCards(false);
        roundPane.setVisible(true);
        infoPane.setVisible(false);
        roundLabel.setText("Round " + currentRound);
        currentRound++;
        resetIfNotFirst();
        playerMatController.resetDeckCounter(9);

        activePlayers.addAll(client.getPlayers());
        for (Player player : activePlayers) {
            //logger.info("Inside GameState:" + player.getID());
        }
        phasePane.setCenter(programmingPane);
        othersController.visibleHBoxRegister(true);
    }

    public void changePhaseView(GameState phase) {
        currentPhase = phase;

        switch (phase) {
            case CONSTRUCTION -> phasePane.setCenter(constructionPane);
            case PROGRAMMING -> {
                resetInProgrammingPhase();
            }
            case ACTIVATION -> {
                activationController.changePhaseView("PlayIt");
                currentAction.clear();
                programmingController.reset();
                phasePane.setCenter(activationPane);
                othersController.visibleHBoxRegister(false);
                first = false;
            }
        }
    }

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

            activationController.changePhaseView("PlayIt");
        } catch (IOException e) {
            logger.error("Inner phase View could not be loaded: " + e.getMessage());
        }
    }

    private void setDisplayAction(ArrayList<JSONBody> currentAction) {
        StringBuilder text = new StringBuilder(" ");
        if (currentAction.size() == 1) {
            if (Energy.class.equals(currentAction.get(0).getClass())) {
                text.append("You got energy");
            } else if (Movement.class.equals(currentAction.get(0).getClass())) {
                text.append("You moved 1");
            } else if (PlayerTurning.class.equals(currentAction.get(0).getClass())) {
                text.append("You turned");
            } else text.append("You performed the card on top of your deck");
        } else if (currentAction.size() == 2) {
            if (Movement.class.equals(currentAction.get(0).getClass()) && Movement.class.equals(currentAction.get(1).getClass())) {
                text.append("You moved 2");
            } else if (PlayerTurning.class.equals(currentAction.get(0).getClass()) && PlayerTurning.class.equals(currentAction.get(1).getClass())) {
                text.append("You performed an UTurn");
            } else text.append("You performed the card on top of your deck");
        } else if (currentAction.size() == 3) {
            if (Movement.class.equals(currentAction.get(0).getClass()) && Movement.class.equals(currentAction.get(1).getClass()) && Movement.class.equals(currentAction.get(2).getClass())) {
                text.append("You moved 3");
            } else {
                text.append("You performed the card on top of your deck");
            }
        }

        moveInfo.setText(String.valueOf(text));

    }

    private boolean currentCardIsDamage = false;
    @Override
    public void update(JSONMessage message) {
        switch (message.getType()) {
            case Error -> {
                Error error = (Error) message.getBody();
                Platform.runLater(() -> infoLabel.setText(error.getError()));
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (infoLabel.getText().equals(error.getError())) {
                            Platform.runLater(() -> infoLabel.setText(" "));
                        }
                        t.cancel();
                    }
                }, 2000);
            }
            case GameStarted -> {
                GameStarted gameStarted = (GameStarted) message.getBody();

                gameBoardController.buildMap(gameStarted);
                othersController.createPlayerMats(client.getPlayers());
            }
            case StartingPointTaken -> {
                StartingPointTaken msg = (StartingPointTaken) message.getBody();
                gameBoardController.placeRobotInMap(client.getPlayerFromID(msg.getPlayerID()), Coordinate.parse(msg.getPosition()));
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

                gameBoardController.handleMovement(client.getPlayerFromID(msg.getPlayerID()), Coordinate.parse(msg.getTo()));
            }
            case PlayerTurning -> {
                PlayerTurning pT = (PlayerTurning) message.getBody();
                currentAction.add(pT);

                gameBoardController.handlePlayerTurning(client.getPlayerFromID(pT.getPlayerID()), pT.getDirection());
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
                activationController.changePhaseView("Damage");
                activationController.getPickDamageController().pickDamage(pickDamage);
            }
            case PlayerShooting -> {
                if (activePlayers.isEmpty()) {
                    logger.info("No active players");
                } else {
                    soundHandler.playSoundEffects("Laser", play);
                    gameBoardController.handleShooting(activePlayers);
                    gameBoardController.handleRobotShooting(activePlayers);
                }
            }
            case Reboot -> {
                Reboot reboot = (Reboot) message.getBody();
                Player player = client.getPlayerFromID(reboot.getPlayerID());
                activePlayers.remove(player);
                soundHandler.playSoundEffects("PitSound", play);

                boolean isThisPlayer = reboot.getPlayerID() == client.getThisPlayersID();
                othersController.setRebootLabel(reboot, isThisPlayer);
            }
            case SelectionFinished -> {
                SelectionFinished selectionFinished = (SelectionFinished) message.getBody();
                if (selectionFinished.getPlayerID() == client.getThisPlayersID()) {
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
                    if (registerCard.getPlayerID() == client.getThisPlayersID()){
                        activationController.currentCards(registerCard.getCard());
                    }
                    else otherPlayer.add(registerCard);
                }
                othersController.currentCards(otherPlayer);

                ArrayList<CardType> damageCards = new ArrayList<>();
                damageCards.add(Spam);
                damageCards.add(Virus);
                damageCards.add(Trojan);
                damageCards.add(Worm);

                for (int i = 0; i < currentCards.getActiveCards().size(); i++) {
                    if (currentCards.getActiveCards().get(i).getPlayerID() == client.getThisPlayersID()) {
                        if(damageCards.contains(currentCards.getActiveCards().get(i).getCard())){
                            playerMatController.subtractPlayerCards(1);
                            currentCardIsDamage = true;
                            System.out.println("currentCardIsDamage = true;");
                        } else {
                            currentCardIsDamage = false;
                            System.out.println("currentCardIsDamage = false;");


                        }
                    }
                }
            }
            case CurrentPlayer -> {
                CurrentPlayer currentPlayer = (CurrentPlayer) message.getBody();
                boolean isThisPlayer = currentPlayer.getPlayerID() == client.getThisPlayersID();

                if (currentPhase == GameState.CONSTRUCTION) {

                    constructionController.currentPlayer(isThisPlayer);
                } else if (currentPhase == GameState.ACTIVATION) {
                    if (currentCardIsDamage) {
                        setDisplayAction(currentAction);
                        infoPane.setVisible(true);
                    } else {
                        infoPane.setVisible(false);
                    }
                    currentAction.clear();
                    activationController.getPlayCardController().currentPlayer(isThisPlayer);
                    othersController.setInfoLabel(currentPlayer, isThisPlayer);
                }
            }
            case Energy -> {
                Energy energy = (Energy) message.getBody();
                if (energy.getPlayerID() == client.getThisPlayersID()) {
                    playerMatController.addEnergy(energy.getCount());
                    currentAction.add(energy);

                } else {
                    othersController.addEnergy(energy);
                }
            }
            case CheckpointReached -> {
                CheckpointReached checkpointsReached = (CheckpointReached) message.getBody();
                if (checkpointsReached.getPlayerID() == client.getThisPlayersID()) {
                    playerMatController.checkPointReached(checkpointsReached.getNumber());
                } else {
                    othersController.checkPointReached(checkpointsReached);
                }
                soundHandler.playSoundEffects("CheckPoint", play);
            }
            case ShuffleCoding -> {
                ShuffleCoding shuffleCoding = (ShuffleCoding) message.getBody();
                if(shuffleCoding.getPlayerID() == client.getThisPlayersID()){
                    infoPane.setVisible(true);
                    Platform.runLater(() -> infoLabel.setText("You're cards are shuffled"));
                    Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (infoLabel.getText().equals("You're cards are shuffled")) {
                                Platform.runLater(() -> infoPane.setVisible(false));
                            }
                            t.cancel();
                        }
                    }, 2000);
                } else {
                    othersController.setShuffleCodingLabel(shuffleCoding);
                }
            }
            case DiscardHand -> {
                DiscardHand discardHand = (DiscardHand) message.getBody();
                if (discardHand.getPlayerID() == client.getThisPlayersID()) {
                    playerMatController.setDiscardDeckCounter(5);
                    playerMatController.resetDeckCounter(5);
                }
            }
            case SelectDamage -> {
                SelectDamage selectDamage = (SelectDamage) message.getBody();
                playerMatController.setDiscardDeckCounter(selectDamage.getCards().size());
            }
            case DrawDamage -> {
                DrawDamage drawDamage = (DrawDamage) message.getBody();
                boolean isThisPlayer = drawDamage.getPlayerID() == client.getThisPlayersID();
                handleDamageCount(drawDamage.getCards());
                if (isThisPlayer) {
                    setDrawDamage(drawDamage);
                    playerMatController.setDiscardDeckCounter(drawDamage.getCards().size());
                    playerMatController.addPlayerCards(drawDamage.getCards().size());
                }
                othersController.setDrewDamageLabel(drawDamage, isThisPlayer);
            }
            case GameWon -> {
                GameWon gameWon = (GameWon) message.getBody();
                phasePane.setCenter(gameWonPane);
                gameWonController.setWinnerLabel(client.getPlayerFromID(gameWon.getPlayerID()));
                soundHandler.playSoundEffects("CheckPoint", play);
            }
        }
    }

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
            client.sendMessage(new SendChat("#r " + orientation, -1));
        }
    }

    public void removePlayer(Player player) {
        gameBoardController.removePlayer(player);
        if (client.getCurrentController().equals(this)) othersController.removePlayer(player);
    }

    @FXML
    private void soundsOnAction() {
        soundHandler.musicOn();
    }

    @FXML
    private void soundsOffAction() {
        soundHandler.musicOff();
    }

    public PlayerMatController getPlayerMatController() {
        return playerMatController;
    }
}