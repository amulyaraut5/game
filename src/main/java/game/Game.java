package game;

import game.gameObjects.decks.SpamDeck;
import game.gameObjects.decks.TrojanDeck;
import game.gameObjects.decks.VirusDeck;
import game.gameObjects.decks.WormDeck;
import game.gameObjects.maps.DizzyHighway;
import game.gameObjects.maps.ExtraCrispy;
import game.gameObjects.maps.Map;
import game.gameObjects.maps.MapBuilder;
import game.gameObjects.robot.Robot;
import game.round.ActivationPhase;
import game.round.ConstructionPhase;
import game.round.ProgrammingPhase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;
import server.User;
import utilities.Coordinate;
import utilities.JSONProtocol.body.ActivePhase;
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.Movement;
import utilities.JSONProtocol.body.ReceivedChat;
import utilities.MapConverter;
import utilities.RegisterCard;
import utilities.enums.GameState;

import java.util.ArrayList;

import static utilities.Coordinate.parse;

/**
 * This class handles the game itself.
 * It saves all the different assets like decks, players etc. and the game is started from here.
 */

public class Game {
    private static final Logger logger = LogManager.getLogger();
    private static Game instance;

    private final Server server = Server.getInstance();
    private ArrayList<Player> players;

    private SpamDeck spamDeck;
    private VirusDeck virusDeck;
    private WormDeck wormDeck;
    private TrojanDeck trojanDeck;

    private Map map;

    private ConstructionPhase constructionPhase;
    private ProgrammingPhase programmingPhase;
    private ActivationPhase activationPhase;
    private GameState gameState;

    /**
     * Shows if a game has already been created or not (false = not created)
     **/
    private boolean createdGame = false;
    /**
     * Shows if a game has already been started or not (false = not running)
     **/
    private boolean runningGame = false;

    private Game() {
    }

    public static Game getInstance() {
        if (instance == null) instance = new Game();
        return instance;
    }

    /**
     * Initialises the game attributes at the beginning of each game
     */
    public void reset() {
        gameState = GameState.CONSTRUCTION;
        players = new ArrayList<>(6);
        createdGame = false;
        runningGame = false;

        spamDeck = new SpamDeck();
        virusDeck = new VirusDeck();
        wormDeck = new WormDeck();
        trojanDeck = new TrojanDeck();
    }

    /**
     * This methods starts RoboRally.
     */
    public void play() {
        reset();

        ArrayList<User> users = server.getUsers();
        for (User user : users) {
            int figure = user.getFigure();
            players.add(new Player(user));
        }
        //map = MapBuilder.constructMap(new DizzyHighway());
        server.communicateAll(MapConverter.convert(map));
        server.communicateAll(new ActivePhase(gameState));
        constructionPhase = new ConstructionPhase();
    }

    public void handleMapSelection(String selectedMap) {
        if (selectedMap.equals("DizzyHighway")) {
            map = MapBuilder.constructMap(new DizzyHighway());
        } else if (selectedMap.equals("ExtraCrispy")) {
            map = MapBuilder.constructMap(new ExtraCrispy());
        }
    }


    /**
     * This method gets called from the phases, it calls the next phase
     */
    public void nextPhase() {
        gameState = gameState.getNext();
        server.communicateAll(new ActivePhase(gameState));

        switch (gameState) {
            case PROGRAMMING -> programmingPhase = new ProgrammingPhase();
            case ACTIVATION -> activationPhase = new ActivationPhase();
        }
    }

    public Player userToPlayer(User user) {
        for (Player player : players) {
            if (user.equals(player)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Gets a player based on their ID from the list of players saved in {@link Game}.
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

    /**
     * Handles cheat messages received from the chat
     *
     * @param message including the # and the cheat
     * @param user    user who sent the cheat
     */
    public void handleCheat(String message, User user) {
        String cheat = message;
        String[] cheatInfo = new String[0];
        if (message.contains(" ")) {
            cheat = message.substring(0, message.indexOf(" "));
            cheatInfo = message.substring(message.indexOf(" ") + 1).split(" ");
        }
        switch (cheat) {
            case "#endTimer" -> programmingPhase.endProgrammingTimer();
            //activates the board - only when activation phase was reached at least once
            case "#activateBoard" -> activationPhase.activateBoard();
            case "#tp" -> {
                Robot robot = userToPlayer(user).getRobot();
                if (cheatInfo.length == 1) {
                    int position = Integer.parseInt(cheatInfo[0]);
                    robot.setCoordinate(parse(position));
                    server.communicateAll(new Movement(user.getID(), position));
                } else if (cheatInfo.length == 2) {
                    int x = Integer.parseInt(cheatInfo[0]);
                    int y = Integer.parseInt(cheatInfo[1]);
                    Coordinate coordinate = new Coordinate(x, y);
                    robot.setCoordinate(coordinate);
                    server.communicateAll(new Movement(user.getID(), coordinate.toPosition()));
                }
            }
            case "#damage" -> {
                Robot robot = userToPlayer(user).getRobot();
                if (cheatInfo.length == 0)
                    server.communicateDirect(new Error("your cheat is invalid!"), user.getID());
                else activationPhase.drawDamage(spamDeck, userToPlayer(user), Integer.parseInt(cheatInfo[0]));
            }
            case "#autoPlay" -> {
                for (int register = 1; register < 6; register++) {
                    for (int i = 0; i < players.size(); i++) {
                        RegisterCard registerCard = activationPhase.getCurrentCards().get(0);
                        activationPhase.activateCards(registerCard.getPlayerID());
                    }
                }
            }
            case "#emptySpam" -> {
                spamDeck.getDeck().clear();
                logger.info("SpamDeckCheat: " + spamDeck.getDeck().size());
            }
            case "#cheats" -> {
                String cheats = """
                                                
                        ----------------------------------------
                        Cheats
                        ----------------------------------------
                        #cheats         | lists all cheats
                        #activateBoard  | activates the board
                        #endTimer       | ends the timer
                        #tp <position>  | teleports the robot
                        #tp <x> <y>     | teleports the robot
                        #damage <x>     | deals given number of spam cards
                        #autoPlay       | autoplay activation phase
                        #emptySpam      | empties the Spam deck
                        ----------------------------------------
                        """;
                user.message(new ReceivedChat(cheats, user.getID(), false));
            }
            default -> server.communicateDirect(new Error("your cheat is invalid!"), user.getID());
        }
    }

    public ConstructionPhase getConstructionPhase() {
        return constructionPhase;
    }

    public ActivationPhase getActivationPhase() {
        return activationPhase;
    }

    public ProgrammingPhase getProgrammingPhase() {
        return programmingPhase;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Map getMap() {
        return map;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public SpamDeck getSpamDeck() {
        return spamDeck;
    }

    public VirusDeck getVirusDeck() {
        return virusDeck;
    }

    public WormDeck getWormDeck() {
        return wormDeck;
    }

    public TrojanDeck getTrojanHorseDeck() {
        return trojanDeck;
    }
}