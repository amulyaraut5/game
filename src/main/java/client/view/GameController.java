package client.view;

import game.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Coordinate;
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

    private final ArrayList<Player> players = client.getPlayers();
    private final ArrayList<Player> activePlayers = new ArrayList<>();

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
    private int interval;
    private int currentRound = 1;
    private boolean first = true;

    private int countSpamCards = 38;
    private int countTrojanCards = 12;
    private int countWormCards = 6;
    private int countVirusCards = 18;

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

    public void changePhaseView(GameState phase) {
        currentPhase = phase;

        switch (phase) {
            case CONSTRUCTION -> phasePane.setCenter(constructionPane);
            case PROGRAMMING -> {
                //playerMatController.fixSelectedCards(false);
                roundPane.setVisible(true);
                roundLabel.setText("Round " + currentRound);
                currentRound++;
                if (!first) {
                    playerMatController.reset();
                    playerMatController.setDiscardDeckCounter(5);

                    othersController.reset();
                    activationController.reset();
                }
                playerMatController.resetDeckCounter(9);

                activePlayers.addAll(players);
                for (Player player : activePlayers) {
                    //logger.info("Inside GameState:" + player.getID());
                }

                phasePane.setCenter(programmingPane);
                othersController.visibleHBoxRegister(true);
            }
            case ACTIVATION -> {
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
        } catch (IOException e) {
            logger.error("Inner phase View could not be loaded: " + e.getMessage());
        }
    }

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
                gameBoardController.handleMovement(client.getPlayerFromID(msg.getPlayerID()), Coordinate.parse(msg.getTo()));
            }
            case PlayerTurning -> {
                PlayerTurning pT = (PlayerTurning) message.getBody();
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
                activationController.pickDamage(pickDamage, this);
            }
            case PlayerShooting -> {
                if (activePlayers.isEmpty()) {
                    logger.info("No active players");
                } else {
                    gameBoardController.handleShooting(activePlayers);
                    gameBoardController.handleRobotShooting(activePlayers);
                }
            }
            case Reboot -> {
                Reboot reboot = (Reboot) message.getBody();
                Player player = client.getPlayerFromID(reboot.getPlayerID());
                //rebootingPlayers.add(player);
                activePlayers.remove(player);
                for (Player player1 : activePlayers) {
                    logger.info("Inside Reboot:" + player1.getID());
                }
                if (activePlayers.isEmpty()) {
                    logger.info("empty");
                }
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
            case TimerStarted -> {
                programmingController.startTimer(allRegistersAsFirst);
            }
            case TimerEnded -> {
                programmingController.setTimerEnded(true);
                playerMatController.setDiscardDeckCounter(4);
                if (!allRegistersAsFirst) playerMatController.fixSelectedCards();
                allRegistersAsFirst = false; //TODO everything that is round related
            }
            case CurrentCards -> {
                CurrentCards currentCards = (CurrentCards) message.getBody();
                ArrayList<RegisterCard> otherPlayer = new ArrayList<>();
                for (RegisterCard registerCard : currentCards.getActiveCards()) {
                    if (registerCard.getPlayerID() == client.getThisPlayersID())
                        activationController.currentCards(registerCard.getCard());
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
                        for (CardType damageCard : damageCards) {
                            if (currentCards.getActiveCards().get(i).getCard() == damageCard)
                                playerMatController.subtractPlayerCards(1);
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
                    activationController.currentPlayer(isThisPlayer);
                    othersController.setInfoLabel(currentPlayer, isThisPlayer);
                }
            }
            case Energy -> {
                Energy energy = (Energy) message.getBody();
                if (energy.getPlayerID() == client.getThisPlayersID()) {
                    playerMatController.addEnergy(energy.getCount());
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
            }
            case ShuffleCoding -> {
                ShuffleCoding shuffleCoding = (ShuffleCoding) message.getBody();
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
                handleDamageCount(drawDamage.getCards());
                if (drawDamage.getPlayerID() == client.getThisPlayersID()) {
                    setDrawDamage(drawDamage);
                    playerMatController.setDiscardDeckCounter(drawDamage.getCards().size());
                    playerMatController.addPlayerCards(drawDamage.getCards().size());
                }
            }
            case GameWon -> {
                GameWon gameWon = (GameWon) message.getBody();
                phasePane.setCenter(gameWonPane);
                gameWonController.setWinnerLabel(client.getPlayerFromID(gameWon.getPlayerID()));
            }
        }
    }

    public void handleDamageCount(CardType cardType) {
        switch (cardType) {
            case Spam -> countSpamCards--;
            case Trojan -> countTrojanCards--;
            case Worm -> countWormCards--;
            case Virus -> countVirusCards--;
        }
    }

    public void handleDamageCount(ArrayList<CardType> cardList) {
        for (CardType cardType : cardList) {
            handleDamageCount(cardType);
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

    public int getCountSpamCards() {
        return countSpamCards;
    }

    public int getCountTrojanCards() {
        return countTrojanCards;
    }

    public int getCountWormCards() {
        return countWormCards;
    }

    public int getCountVirusCards() {
        return countVirusCards;
    }
}