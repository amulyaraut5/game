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
import game.round.ProgrammingPhase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;
import server.User;
import utilities.Coordinate;
import utilities.JSONProtocol.body.ActivePhase;
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.Movement;
import utilities.JSONProtocol.body.StartingPointTaken;
import utilities.MapConverter;
import utilities.Utilities;
import utilities.enums.AttributeType;
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
    private int energyBank;
    private ArrayList<Player> players;

    private SpamDeck spamDeck;
    private VirusDeck virusDeck;
    private WormDeck wormDeck;
    private TrojanDeck trojanDeck;

    private Map map;

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

    public ActivationPhase getActivationPhase() {
        return activationPhase;
    }

    public ProgrammingPhase getProgrammingPhase() {
        return programmingPhase;
    }

    /**
     * Initialises the game attributes at the beginning of each game
     */
    public void reset() {
        energyBank = Utilities.ENERGY_BANK;
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

    }

    public void handleMapSelection(String selectedMap){
        if(selectedMap.equals("DizzyHighway")){
            map = MapBuilder.constructMap(new DizzyHighway());
        }else {
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

    public void setStartingPoint(User user, int position) {
        Coordinate pos = parse(position);
        Player player = userToPlayer(user);

        if (position < 1 || position > 130) {
            player.message(new Error("This is no viable point on the map!"));
            return;
        }

        //check if playes has already set their starting point
        if (player.getRobot().getCoordinate() == null) {
            //check if chosen tile is StartingPoint
            System.out.println(pos);
            boolean isOnStartPoint = map.getTile(pos).hasAttribute(AttributeType.StartPoint);//TODO Index -1 out of bounds for length 13
            if (isOnStartPoint) {
                //check if no other player is on the chosen tile
                for (Player other : players) {
                    if (!other.equals(player)) {
                        Coordinate otherPos = other.getRobot().getCoordinate();
                        if (!pos.equals(otherPos)) {
                            //chosen StartingPoint is valid
                            player.getRobot().setCoordinate(pos);
                            server.communicateAll(new StartingPointTaken(player.getID(), position));
                        } else player.message(new Error("Your chosen position is already taken!"));
                    }
                }
            } else player.message(new Error("This is no valid StartPoint!"));
        } else player.message(new Error("You have already set your starting point!"));

        //check if all players have set their StartingPoint
        for (Player p : players) {
            if (p.getRobot().getCoordinate() == null) return;
        }
        nextPhase();
    }

    public Player userToPlayer(User user) {
        for (Player player : players) {
            if (user.equals(player)) {
                return player;
            }
        }
        return null;
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

    public GameState getGameState() {
        return gameState;
    }

    /**
     * Handles cheat messages received from the chat
     *
     * @param message including the # and the cheat
     * @param user user who sent the cheat
     */
    public void handleCheat(String message, User user) {
        String cheat = message;
        String cheatInfo = message;
        if (message.contains(" ")) {
            cheat = message.substring(0, message.indexOf(" "));
            cheatInfo = message.substring(message.indexOf(" ")+1);
        }
        switch (cheat) {
            case "#endTimer":
                programmingPhase.endProgrammingTimer();
                break;
            case "#teleport":
                Robot robot = userToPlayer(user).getRobot();
                int position = Integer.parseInt(cheatInfo);
                robot.setCoordinate(parse(position));

                server.communicateAll(new Movement(user.getID(), position));
                break;
            default: server.communicateDirect(new Error("your cheat is invalid"), user.getID() );
        }
    }


}