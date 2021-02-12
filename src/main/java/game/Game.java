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
import utilities.Constants;
import utilities.Coordinate;
import utilities.JSONProtocol.body.ActivePhase;
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.ReceivedChat;
import utilities.MapConverter;
import utilities.RegisterCard;
import utilities.enums.GameState;
import utilities.enums.Orientation;

import java.util.ArrayList;

/**
 * This class handles the game itself.
 * It saves all the different assets like decks, players etc. and the game is started from here.
 * @author janau
 */

public class Game {
    private static final Logger logger = LogManager.getLogger();
    private static Game instance;

    private final Server server = Server.getInstance();
    private ArrayList<Player> players;
    private ArrayList<Player> activePlayers;

    private SpamDeck spamDeck;
    private VirusDeck virusDeck;
    private WormDeck wormDeck;
    private TrojanDeck trojanDeck;

    private Map map;

    private ConstructionPhase constructionPhase;
    private ProgrammingPhase programmingPhase;
    private ActivationPhase activationPhase;
    private GameState gameState;

    private Game() {
    }

    /**
     * Initialises the game attributes at the beginning of each game
     */
    public void reset() {
        gameState = GameState.CONSTRUCTION;
        players = new ArrayList<>(6);
        activePlayers = new ArrayList<>(6);

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

        handleMapSelection(server.getSelectedMap());

        ArrayList<User> users = server.getUsers();
        for (User user : users) {
            players.add(new Player(user));
            activePlayers.add(userToPlayer(user));
        }
        server.communicateAll(MapConverter.convert(map));
        server.communicateAll(new ActivePhase(gameState));
        constructionPhase = new ConstructionPhase();
    }

    public void handleMapSelection(String selectedMap) {
        switch (selectedMap) {
            case ("DizzyHighway") -> map = MapBuilder.constructMap(new DizzyHighway());
            case ("ExtraCrispy") -> map = MapBuilder.constructMap(new ExtraCrispy());
        }
    }

    /**
     * This method gets called from the phases, it calls the next phase
     */
    public void nextPhase() {
        gameState = gameState.getNext();
        server.communicateAll(new ActivePhase(gameState));

        switch (gameState) {
            case UPGRADE -> nextPhase();
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
        Player player = userToPlayer(user);
        String cheat = message;
        String[] cheatInfo = new String[0];
        if (message.contains(" ")) {
            cheat = message.substring(0, message.indexOf(" "));
            cheatInfo = message.substring(message.indexOf(" ") + 1).split(" ");
        }

        if (cheat.equals("#cheats")) {
            if (cheatInfo.length == 0) {
                user.message(new ReceivedChat(Constants.CHEAT_LIST, user.getID(), false));
            } else {
                if (cheatInfo[0].equals("on") && !player.isUsingCheats()) {
                    player.setUsingCheats(true);
                    server.communicateAll(new Error(user + " turned on cheats!"));
                } else if (cheatInfo[0].equals("off") && player.isUsingCheats()) {
                    player.setUsingCheats(false);
                    server.communicateAll(new Error(user + " turned off cheats!"));
                }
            }
        } else if (player.isUsingCheats()) {
            activateCheat(user, cheat, cheatInfo);
        } else server.communicateDirect(new Error("Please activate cheats first"), user.getID());
    }

    private void activateCheat(User user, String cheat, String[] cheatInfo) {
        switch (cheat) {
            case "#endTimer" -> programmingPhase.endProgrammingTimer();
            //activates the board - only when activation phase was reached at least once
            case "#activateBoard" -> {
                try {
                    activationPhase.activateBoard();
                } catch (NullPointerException e) {
                    server.communicateDirect(new Error("You must wait till you get to activation phase."), user.getID());
                }
            }

            case "#tp" -> {
                Robot robot = userToPlayer(user).getRobot();
                if (cheatInfo.length == 1) {
                    int position = Integer.parseInt(cheatInfo[0]);
                    try {
                        if (map.isAttributeOn(position))
                            server.communicateDirect(new Error("Movement is not allowed."), user.getID());
                        else if (activationPhase.isPositionFree(position))
                            robot.moveTo(Coordinate.parse(position));
                        else server.communicateDirect(new Error("Movement is not allowed."), user.getID());
                    } catch (NullPointerException e) {
                        server.communicateDirect(new Error("You must wait till you get to activation phase."), user.getID());
                    }
                } else if (cheatInfo.length == 2) {
                    int x = Integer.parseInt(cheatInfo[0]);
                    int y = Integer.parseInt(cheatInfo[1]);
                    Coordinate coordinate = new Coordinate(x, y);
                    try {
                        if (map.isAttributeOn(coordinate))
                            server.communicateDirect(new Error("Movement is not allowed."), user.getID());
                        else if (activationPhase.isPositionFree(coordinate))
                            robot.moveTo(coordinate);
                        else server.communicateDirect(new Error("Movement is not allowed."), user.getID());
                    } catch (NullPointerException e) {
                        server.communicateDirect(new Error("You must wait till you get to activation phase."), user.getID());
                    }
                }
            }
            case "#r" -> {
                Robot robot = userToPlayer(user).getRobot();
                if (cheatInfo.length == 1) {
                    Orientation orientation = robot.getOrientation();
                    switch (cheatInfo[0]) {
                        case "u" -> orientation = Orientation.UP;
                        case "r" -> orientation = Orientation.RIGHT;
                        case "d" -> orientation = Orientation.DOWN;
                        case "l" -> orientation = Orientation.LEFT;
                    }
                    robot.rotateTo(orientation);
                }
            }
            case "#damage" -> {
                if (cheatInfo.length == 0)
                    server.communicateDirect(new Error("your cheat is invalid!"), user.getID());
                else if (getActivationPhase() == null)
                    server.communicateDirect(new Error("your cheat is invalid in this phase"), user.getID());
                else activationPhase.drawDamage(spamDeck, userToPlayer(user), Integer.parseInt(cheatInfo[0]));
            }
            case "#damageDecks" -> {
                String cardDeck = "\n  | Spam Deck   " + spamDeck.size() +
                        "\n  | Trojan Deck  " + trojanDeck.size() +
                        "\n  | Worm Deck   " + wormDeck.size() +
                        "\n  | Virus Deck   " + virusDeck.size();
                user.message(new ReceivedChat(cardDeck, user.getID(), false));
            }
            case "#autoPlay" -> {
                for (int register = 1; register < 6; register++) {
                    try {
                        for (int i = 0; i < activationPhase.getCurrentCards().size() + 1; i++) {
                            RegisterCard registerCard = activationPhase.getCurrentCards().get(0);
                            activationPhase.activateCards(registerCard.getPlayerID());
                        }
                    } catch (IndexOutOfBoundsException e) {
                        logger.info("autoPlay - end of current cards");
                    }
                }
            }
            case "#fire" -> {
                try {
                    activationPhase.activateCheatLaser();
                } catch (NullPointerException e) {
                    server.communicateDirect(new Error("You must wait till you get to activation phase."), user.getID());
                }
            }

            case "#win" -> server.gameWon(user.getID());
            case "#emptySpam" -> {
                spamDeck.getDeck().clear();
                logger.info("SpamDeckCheat: " + spamDeck.getDeck().size());
                activationPhase.drawDamage(spamDeck, userToPlayer(user), spamDeck.size());
            }
            default -> server.communicateDirect(new Error("your cheat is invalid!"), user.getID());
        }
    }

    public static Game getInstance() {
        if (instance == null) instance = new Game();
        return instance;
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

    public ArrayList<Player> getActivePlayers() {
        return activePlayers;
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

    public TrojanDeck getTrojanDeck() {
        return trojanDeck;
    }
}