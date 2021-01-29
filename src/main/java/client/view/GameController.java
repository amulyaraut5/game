package client.view;

import game.Player;
import game.gameObjects.maps.Map;
import game.gameObjects.robot.Robot;
import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Empty;
import game.gameObjects.tiles.Laser;
import game.gameObjects.tiles.Wall;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.*;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.*;
import utilities.enums.AttributeType;
import utilities.enums.GameState;
import utilities.enums.Orientation;
import utilities.enums.Rotation;

import java.io.IOException;
import java.util.*;

import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;

/**
 * The GameViewController class controls the GameView and coordinates its inner views
 *
 * @author Simon
 * @author Sarah
 * @author Amulya
 */
public class GameController extends Controller implements Updatable {
    private static final Logger logger = LogManager.getLogger();

    private final Group[][] fields = new Group[Utilities.MAP_WIDTH][Utilities.MAP_HEIGHT];
    private ArrayList<Coordinate> path = new ArrayList<>();
    private HashMap<Player, ImageView> robotTokens = new HashMap<>();
    private Map map;

    private PlayerMatController playerMatController;
    private ConstructionController constructionController;
    private ProgrammingController programmingController;
    private ActivationController activationController;
    private OthersController othersController;

    private Pane constructionPane;
    private Pane programmingPane;
    private Pane activationPane;

    private SoundHandler soundHandler;
    private EventHandler<MouseEvent> onMapClicked;

    private GameState currentPhase = GameState.CONSTRUCTION;

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
    private StackPane boardPane; //stacks the map-, animation-, and playerPane
    @FXML
    private FlowPane mapPane;
    @FXML
    private Pane animationPane;
    @FXML
    private Pane robotPane;

    @FXML
    private Label infoLabel;
    @FXML
    private AnchorPane drawDamageAnchorPane;
    @FXML
    private Label drawDamageLabel;
    @FXML
    private HBox drawDamageHBox;
    private int interval;
    private int currentRound = 1;
    private boolean first = true;

    @FXML
    public void initialize() {
        drawDamageHBox.setAlignment(Pos.CENTER);
        drawDamageHBox.setSpacing(5);
        drawDamageAnchorPane.setVisible(false);
        constructPhaseViews();
        roundPane.setVisible(false);
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

        boardPane.addEventHandler(MOUSE_CLICKED, onMapClicked = mouseEvent -> {
            int x = (int) mouseEvent.getX() / Utilities.FIELD_SIZE;
            int y = (int) mouseEvent.getY() / Utilities.FIELD_SIZE;
            int position = new Coordinate(x, y).toPosition();
            client.sendMessage(new SetStartingPoint(position));
        });

        this.soundHandler = new SoundHandler();
    }

    public void drawDamage(DrawDamage drawDamage) {

    }

    public void attachChatPane(Pane chat) {
        chat.setPrefWidth(chatPane.getPrefWidth());
        chat.setPrefHeight(chatPane.getPrefHeight());
        chatPane.setCenter(chat);
    }

    public void buildMap(GameStarted gameStarted) {
        map = MapConverter.reconvert(gameStarted);
        int xMax = Utilities.MAP_WIDTH;
        int yMax = Utilities.MAP_HEIGHT;

        for (int y = 0; y < yMax; y++) {
            for (int x = 0; x < xMax; x++) {
                ArrayList<Attribute> attributes = map.getTile(x, y).getAttributes();
                fields[x][y] = new Group();

                if (isTileBackgroundEmpty(attributes)) {    //If the image would be transparent, an empty tile is added.
                    fields[x][y].getChildren().add(new Empty().createImage());
                }

                attributes = sortBoardAttributes(attributes);
                for (Attribute attribute : attributes) {
                    Node attributeImage = attribute.createImage();
                    if (attributeImage != null) {
                        fields[x][y].getChildren().add(attributeImage);
                    }
                }
                mapPane.getChildren().add(fields[x][y]);
            }
        }
    }

    /**
     * This method places the robot on the board based on the starting point player chooses.
     *
     * @param player
     * @param coordinate
     */

    public void placeRobotInMap(Player player, Coordinate coordinate) {
        if (player.getID() == client.getThisPlayersID()) {
            boardPane.removeEventHandler(MOUSE_CLICKED, onMapClicked);
        }
        player.getRobot().setCoordinate(coordinate);
        ImageView imageView = player.getRobot().drawRobotImage();

        imageView.fitWidthProperty().bind(boardPane.widthProperty().divide(Utilities.MAP_WIDTH));
        imageView.fitHeightProperty().bind(boardPane.heightProperty().divide(Utilities.MAP_HEIGHT));
        imageView.setPreserveRatio(true);

        robotPane.getChildren().add(imageView);
        imageView.setX(coordinate.getX() * Utilities.FIELD_SIZE);
        imageView.setY(coordinate.getY() * Utilities.FIELD_SIZE);

        // Stores the imageView of to change its coordinates later.
        robotTokens.put(player, imageView);
    }

    public void setDrawDamage(DrawDamage drawDamage) {
        drawDamageAnchorPane.setVisible(true);
        drawDamageLabel.setText("You got damage! ");
        for (int i = 0; i < drawDamage.getCards().size(); i++) {
            String path = "/cards/programming/" + drawDamage.getCards().get(i).name() + "-card.png";
            ImageView damage = new ImageView(new Image(getClass().getResource(path).toString()));
            damage.setFitWidth(30);
            damage.setFitHeight(50);
            drawDamageHBox.getChildren().add(damage);
        }
        Timer timer = new Timer();
        interval = 5;
        timer.schedule(new TimerTask() {
            public void run() {
                if (interval > 0) interval--;
                else {
                    timer.cancel();
                    drawDamageAnchorPane.setVisible(false);
                }
            }
        }, 1000, 1000);
    }

    /**
     * This method moves the player in the view based on the argument received
     * and updates the player position after every method call on the client side to keep track of position
     * to update the view later.
     *
     * @param player Player whose robot should be moved
     * @param newPos new Position of robot after movement is handled
     */
    public void handleMovement(Player player, Coordinate newPos) {
        ImageView imageView = robotTokens.get(player);
        Coordinate oldPos = player.getRobot().getCoordinate();
        player.getRobot().setCoordinate(newPos);

        imageView.setX(oldPos.getX() * Utilities.FIELD_SIZE);
        imageView.setY(oldPos.getY() * Utilities.FIELD_SIZE);

        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(1));
        transition.setNode(imageView);
        transition.setToX((newPos.getX() - oldPos.getX()) * Utilities.FIELD_SIZE);
        transition.setToY((newPos.getY() - oldPos.getY()) * Utilities.FIELD_SIZE);
        transition.setOnFinished(event -> {
            imageView.setX((oldPos.getX() * Utilities.FIELD_SIZE) + imageView.getTranslateX());
            imageView.setY((oldPos.getY() * Utilities.FIELD_SIZE) + imageView.getTranslateY());
            imageView.setTranslateX(0);
            imageView.setTranslateY(0);
        });
        transition.play();
    }

    /**
     * This method rotates the player in the view based on the argument received and updates
     * the player orientation after every method call on the client side to keep track of
     * orientation to update the view later.
     *
     * @param player   Player whose robot should be turned
     * @param rotation Parameter that determines how the player should be rotated.
     */

    public void handlePlayerTurning(Player player, Rotation rotation) {
        ImageView imageView = robotTokens.get(player);
        int angle = 0;
        Robot r = player.getRobot();
        if (rotation == Rotation.LEFT) {
            angle = -90;
            r.setOrientation(r.getOrientation().getPrevious());
        } else if (rotation == Rotation.RIGHT) {
            angle = 90;
            r.setOrientation(r.getOrientation().getNext());
        }

        RotateTransition transition = new RotateTransition();
        transition.setDuration(Duration.seconds(1));
        transition.setNode(imageView);
        transition.setByAngle(angle);

        transition.setOnFinished(event -> {
            switch (r.getOrientation()) {
                case UP -> imageView.setRotate(0);
                case DOWN -> imageView.setRotate(180);
                case RIGHT -> imageView.setRotate(90);
                case LEFT -> imageView.setRotate(270);
            }
        });

        transition.play();
    }

    /**
     * This method only shows the animation of laser beam being fired in the view.
     * The laser beam will terminate abruptly if it hits the robot or wall on it's way.
     *
     * @param players active player list
     */

    public void handleShooting(ArrayList<Player> players) {
        for (Coordinate c : map.readLaserCoordinates()) {
            for (Attribute a : map.getTile(c).getAttributes()) {
                ImageView imageView = new ImageView(new Image(getClass().getResource("/tiles/laser/animation/laserBeam_1.png").toExternalForm()));
                imageView.fitWidthProperty().bind(boardPane.widthProperty().divide(Utilities.MAP_WIDTH));
                imageView.fitHeightProperty().bind(boardPane.heightProperty().divide(Utilities.MAP_HEIGHT));
                imageView.setPreserveRatio(true);
                robotPane.getChildren().add(imageView);

                if (a.getType() == AttributeType.Laser) {
                    Orientation orientation = ((Laser) a).getOrientation();
                    switch (orientation) {
                        case LEFT, RIGHT -> imageView.setRotate(270);
                    }

                    Coordinate newPos = calculateEndCoordinate(orientation, c, players);

                    imageView.setX(c.getX() * Utilities.FIELD_SIZE);
                    imageView.setY(c.getY() * Utilities.FIELD_SIZE);

                    TranslateTransition transition = new TranslateTransition();
                    transition.setDuration(Duration.seconds(4));
                    transition.setNode(imageView);
                    transition.setToX((newPos.getX() - c.getX()) * Utilities.FIELD_SIZE);
                    transition.setToY((newPos.getY() - c.getY()) * Utilities.FIELD_SIZE);
                    transition.setOnFinished(e -> robotPane.getChildren().remove(imageView));

                    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), imageView);
                    fadeTransition.setFromValue(1.0f);
                    fadeTransition.setToValue(0.3f);
                    fadeTransition.setOnFinished(e -> robotPane.getChildren().remove(imageView));

                    SequentialTransition sequentialTransition = new SequentialTransition();
                    sequentialTransition.getChildren().addAll(transition, fadeTransition);
                    sequentialTransition.play();
                    path.clear();
                }
            }
        }
    }

    /**
     * This method only shows the animation of laser beam being fired in the view.
     * The laser beam will terminate abruptly if it hits the robot or wall on it's way.
     * The robot can fire from any position as long as it is playing in the current round.
     *
     * @param players active player list
     */

    public void handleRobotShooting(ArrayList<Player> players) {
        for (Player player : players) {
            Orientation orientation = player.getRobot().getOrientation();
            Coordinate robotPosition = player.getRobot().getCoordinate();

            ImageView imageView = new ImageView(new Image(getClass().getResource("/tiles/laser/animation/laserBeam_1.png").toExternalForm()));
            imageView.fitWidthProperty().bind(boardPane.widthProperty().divide(Utilities.MAP_WIDTH));
            imageView.fitHeightProperty().bind(boardPane.heightProperty().divide(Utilities.MAP_HEIGHT));
            imageView.setPreserveRatio(true);
            robotPane.getChildren().add(imageView);

            switch (orientation) {
                case LEFT, RIGHT -> imageView.setRotate(270);
            }

            Coordinate newPos = calculateRobotEndCoordinate(orientation, robotPosition, players);
            imageView.setX(robotPosition.getX() * Utilities.FIELD_SIZE);
            imageView.setY(robotPosition.getY() * Utilities.FIELD_SIZE);

            TranslateTransition transition = new TranslateTransition();
            transition.setDuration(Duration.seconds(4));
            transition.setNode(imageView);
            transition.setToX((newPos.getX() - robotPosition.getX()) * Utilities.FIELD_SIZE);
            transition.setToY((newPos.getY() - robotPosition.getY()) * Utilities.FIELD_SIZE);
            transition.setOnFinished(e -> robotPane.getChildren().remove(imageView));

            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), imageView);
            fadeTransition.setFromValue(1.0f);
            fadeTransition.setToValue(0.3f);
            fadeTransition.setOnFinished(e -> robotPane.getChildren().remove(imageView));

            SequentialTransition sequentialTransition = new SequentialTransition();
            sequentialTransition.getChildren().addAll(transition, fadeTransition);
            sequentialTransition.play();
            path.clear();
        }
    }

    /**
     * It calculates the last possible coordinate where laser terminates for the robot.
     *
     * @param orientation Direction at which robot is facing
     * @param position    Position of robot in board.
     * @param players     active players list from the game
     * @return Coordinate
     */

    private Coordinate calculateRobotEndCoordinate(Orientation orientation, Coordinate position, ArrayList<Player> players) {
        position = position.clone();
        Coordinate step = orientation.toVector();

        outerLoop:
        while (true) {
            position.add(step);
            if (position.isOutOfBound()) {
                logger.info("Laser Out of Bound");
                break outerLoop;
            } else {
                for (Attribute b : map.getTile(position.getX(), position.getY()).getAttributes()) {
                    if (b.getType() != AttributeType.Wall && b.getType() != AttributeType.Antenna && b.getType() != AttributeType.Laser
                            && b.getType() != AttributeType.ControlPoint) {
                        path.add(position.clone());
                        break;
                    } else if (b.getType() == AttributeType.ControlPoint && b.getType() == AttributeType.Laser) {
                        path.add(position.clone());
                        break outerLoop;
                    } else if (b.getType() == AttributeType.Laser) {
                        if (((Laser) b).getOrientation() == orientation) {
                            break outerLoop;
                        } else if (((Laser) b).getOrientation() == orientation.getOpposite()) {
                            path.add(position.clone());
                            break outerLoop;
                        } else if (((Laser) b).getOrientation() != orientation) {
                            path.add(position.clone());
                            break;
                        }
                    } else if (b.getType() == AttributeType.Wall) {
                        if (((Wall) b).getOrientation() == orientation) {
                            path.add(position.clone());
                            break outerLoop;
                        } else if (((Wall) b).getOrientation() == orientation.getOpposite()) {
                            break outerLoop;
                        } else if (((Wall) b).getOrientation() != orientation) {
                            path.add(position.clone());
                            break;
                        }
                    } else if (b.getType() == AttributeType.Antenna) break outerLoop;
                }
            }
        }
        if (path.size() == 0) {
            logger.info("Nowhere to fire");
            return position;
        } else {
            for (Coordinate coordinate : path) {
                for (Player player : players) {
                    if (coordinate.equals(player.getRobot().getCoordinate())) return coordinate;
                }
            }
            return path.get(path.size() - 1);
        }
    }

    /**
     * It calculates the last possible coordinate where laser terminates for the board lasers.
     *
     * @param orientation Direction at which laser is facing
     * @param position    Position of laser in board
     * @param players     Possible player who could be affected
     * @return Coordinate
     */
    private Coordinate calculateEndCoordinate(Orientation orientation, Coordinate position, ArrayList<Player> players) {

        path.add(position);
        position = position.clone();
        Coordinate step = orientation.toVector();

        outerLoop:
        while (true) {
            position.add(step);
            if (position.isOutOfBound()) {
                logger.info("Laser Out of Bound");
                break outerLoop;
            } else {
                for (Attribute b : map.getTile(position.getX(), position.getY()).getAttributes()) {
                    if (b.getType() != AttributeType.Wall && b.getType() != AttributeType.Antenna && b.getType() != AttributeType.Laser
                            && b.getType() != AttributeType.ControlPoint) {
                        path.add(position.clone());
                        break;
                    } else if (b.getType() == AttributeType.ControlPoint && b.getType() == AttributeType.Laser) {
                        path.add(position.clone());
                        break outerLoop;
                    } else if (b.getType() == AttributeType.Laser) {
                        if (((Laser) b).getOrientation() == orientation) {
                            break outerLoop;
                        } else if (((Laser) b).getOrientation() == orientation.getOpposite()) {
                            path.add(position.clone());
                            break outerLoop;
                        } else if (((Laser) b).getOrientation() != orientation) {
                            path.add(position.clone());
                            break;
                        }
                    } else if (b.getType() == AttributeType.Wall) {
                        if (((Wall) b).getOrientation() == orientation) {
                            path.add(position.clone());
                            break outerLoop;
                        } else if (((Wall) b).getOrientation() == orientation.getOpposite()) {
                            break outerLoop;
                        } else if (((Wall) b).getOrientation() != orientation) {
                            path.add(position.clone());
                            break;
                        }
                    } else if (b.getType() == AttributeType.Antenna) break outerLoop;
                }
            }
        }
        for (Coordinate coordinate : path) {
            for (Player player : players) {
                if (coordinate.equals(player.getRobot().getCoordinate())) return coordinate;
            }
        }
        return path.get(path.size() - 1);
    }

    public ProgrammingController getProgrammingController() {
        return programmingController;
    }

    public ActivationController getActivationController() {
        return activationController;
    }

    public OthersController getOthersController() {
        return othersController;
    }

    /**
     * Method tests if the background of all attributes on a field are transparent.
     *
     * @param field Field to test
     * @return True if the Tile is transparent.
     */
    private boolean isTileBackgroundEmpty(ArrayList<Attribute> field) {
        for (Attribute a : field) {
            if (a.getType() == AttributeType.Empty || a.getType() == AttributeType.Pit) {
                return false;
            }
        }
        return true;
    }

    private ArrayList<Attribute> sortBoardAttributes(ArrayList<Attribute> field) {
        var sortedField = new ArrayList<Attribute>();
        AttributeType[][] priorityArray = {
                {AttributeType.Pit, AttributeType.Empty},
                {AttributeType.Belt, AttributeType.RotatingBelt, AttributeType.Gear,
                        AttributeType.EnergySpace, AttributeType.Antenna},
                {AttributeType.ControlPoint, AttributeType.RestartPoint, AttributeType.StartPoint},
                {AttributeType.PushPanel, AttributeType.Laser},
                {AttributeType.Wall}};

        for (AttributeType[] priority : priorityArray) {
            List<AttributeType> priorityList = Arrays.asList(priority);
            for (Attribute a : field) {
                AttributeType type = a.getType();
                if (priorityList.contains(type)) {
                    sortedField.add(a);
                }
            }
        }
        return sortedField;
    }

    /**
     * Button press to test the change of inner phase panes.
     */
    @FXML
    private void setNextPhaseView() {
        changePhaseView(currentPhase.getNext());
    }

    public void changePhaseView(GameState phase) {
        currentPhase = phase;

        switch (phase) {
            case CONSTRUCTION -> phasePane.setCenter(constructionPane);
            case PROGRAMMING -> {
                getPlayerMatController().fixSelectedCards(false);
                roundPane.setVisible(true);
                roundLabel.setText("Round " + currentRound);
                currentRound++;
                if (!first) {
                    getPlayerMatController().reset();
                    othersController.reset();
                    getActivationController().reset();
                }
                client.setAllRegistersAsFirst(false); //TODO everything that is round related
                phasePane.setCenter(programmingPane);
                othersController.visibleHBoxRegister(true);
            }
            case ACTIVATION -> {
                getPlayerMatController().fixSelectedCards(true);
                getProgrammingController().reset();
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

        try {
            constructionPane = constructionLoader.load();
            programmingPane = programmingLoader.load();
            activationPane = activationLoader.load();

            constructionController = constructionLoader.getController();
            programmingController = programmingLoader.getController();
            activationController = activationLoader.getController();
        } catch (IOException e) {
            logger.error("Inner phase View could not be loaded: " + e.getMessage());
        }
    }

    @FXML
    private void soundsOnAction() {
        this.soundHandler.musicOn();
    }

    @FXML
    private void soundsOffAction() {
        this.soundHandler.musicOff();
    }

    public PlayerMatController getPlayerMatController() {
        return playerMatController;
    }

    public void removePlayer(Player player) {
        ImageView imageView = robotTokens.get(player);
        robotPane.getChildren().remove(imageView);
        //TODO remove small player mat
    }

    @Override
    public void update(JSONMessage message) {
        switch (message.getType()) {
            case Error -> {
                Error error = (Error) message.getBody();
                infoLabel.setText(error.getError());
            }
            case GameStarted -> {
                GameStarted gameStarted = (GameStarted) message.getBody();

                buildMap(gameStarted);
                getOthersController().createPlayerMats(client.getPlayers());
            }
            case StartingPointTaken -> {
                StartingPointTaken msg = (StartingPointTaken) message.getBody();
                placeRobotInMap(client.getPlayerFromID(msg.getPlayerID()), Coordinate.parse(msg.getPosition()));
            }
            case ActivePhase -> {
                ActivePhase activePhase = (ActivePhase) message.getBody();
                changePhaseView(activePhase.getPhase());
            }
            case YourCards -> {
                YourCards yourCards = (YourCards) message.getBody();
                getProgrammingController().startProgrammingPhase(yourCards.getCards());
            }
            case CardsYouGotNow -> {
                CardsYouGotNow cardsYouGotNow = (CardsYouGotNow) message.getBody();
                getPlayerMatController().setNewCardsYouGotNow(cardsYouGotNow);
            }
            case Movement -> {
                Movement msg = (Movement) message.getBody();
                handleMovement(client.getPlayerFromID(msg.getPlayerID()), Coordinate.parse(msg.getTo()));
            }
            case PlayerTurning -> {
                PlayerTurning pT = (PlayerTurning) message.getBody();
                handlePlayerTurning(client.getPlayerFromID(pT.getPlayerID()), pT.getDirection());
            }
            case CardSelected -> {
                CardSelected cardSelected = (CardSelected) message.getBody();
                getOthersController().getOtherPlayerController(cardSelected.getPlayerID()).cardSelected(cardSelected.getRegister());
            }
            case NotYourCards -> {
                NotYourCards notYourCards = (NotYourCards) message.getBody();
                getOthersController().setNotYourCards(notYourCards);
            }
            case PickDamage -> {
                PickDamage pickDamage = (PickDamage) message.getBody();
                getActivationController().pickDamage(pickDamage);
            }
            case PlayerShooting -> {
                ArrayList<Player> players = client.getPlayers();
                handleShooting(players);
                handleRobotShooting(players);
            }
        }
    }
}