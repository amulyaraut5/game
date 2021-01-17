package game;

import game.gameObjects.decks.SpamDeck;
import game.gameObjects.decks.TrojanDeck;
import game.gameObjects.decks.VirusDeck;
import game.gameObjects.decks.WormDeck;
import game.gameObjects.maps.DizzyHighway;
import game.gameObjects.maps.Map;
import game.gameObjects.maps.MapBuilder;
import game.round.ActivationPhase;
import game.round.LaserAction;
import game.round.ProgrammingPhase;
import game.round.UpgradePhase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;
import server.User;
import utilities.Coordinate;
import utilities.JSONProtocol.body.ActivePhase;
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.StartingPointTaken;
import utilities.MapConverter;
import utilities.Utilities;
import utilities.Utilities.AttributeType;
import utilities.Utilities.PhaseState;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class handles the game itself.
 * It saves all the different assets like decks, players etc. and the game is started from here.
 */

public class Game {
    private static final Logger logger = LogManager.getLogger();
    private static Game instance;

    private final Server server = Server.getInstance();
    private int energyBank;
    private UpgradeShop upgradeShop;
    private ArrayList<Player> players;
    private HashMap<Integer, Player> playerIDs = new HashMap<>();

    private SpamDeck spamDeck;
    private VirusDeck virusDeck;
    private WormDeck wormDeck;
    private TrojanDeck trojanDeck;

    private int noOfCheckpoints;
    private Map map;

    private ProgrammingPhase programmingPhase;
    private ActivationPhase activationPhase;
    private UpgradePhase upgradePhase;

    private PhaseState activePhase;

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
        energyBank = Utilities.ENERGY_BANK;
        activePhase = PhaseState.CONSTRUCTION;
        upgradeShop = new UpgradeShop();
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

        map = MapBuilder.constructMap(new DizzyHighway());
        server.communicateAll(MapConverter.convert(map));
        server.communicateAll(new ActivePhase(activePhase));
        new LaserAction().determineLaserPaths();

/*        while (players.size() >= MIN_PLAYERS && players.size() <= MAX_PLAYERS) {
            nextPhase(Utilities.Phase.CONSTRUCTION);
            logger.info("Game has started");
            upgradePhase.startPhase();
            programmingPhase.startPhase();
            activationPhase.startPhase();
        }
        throw new UnsupportedOperationException();*/
    }

    /**
     * This method gets called from the phases, it calls the next phase
     */
    public void nextPhase() {
        activePhase = activePhase.getNext();
        //upgradePhase is skipped in current milestone
        if (activePhase == PhaseState.UPGRADE) activePhase = activePhase.getNext();

        switch (activePhase) {
            case UPGRADE -> upgradePhase = new UpgradePhase();
            case PROGRAMMING -> programmingPhase = new ProgrammingPhase();
            case ACTIVATION -> activationPhase = new ActivationPhase();
        }
        server.communicateAll(new ActivePhase(activePhase));
    }

    public int getNoOfCheckPoints() {
        return this.noOfCheckpoints;
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

    public Player getPlayerFromID(Integer id) {
        return playerIDs.get(id);
    }

    public void setStartingPoint(User user, int position) {
        Coordinate pos = MapConverter.reconvertToCoordinate(position);
        Player player = userToPlayer(user);

        //check if chosen tile is StartingPoint
        boolean isOnStartPoint = map.getTile(pos).hasAttribute(AttributeType.StartPoint);
        if (isOnStartPoint) {
            //check if no other player is on the chosen tile
            for (Player other : players) {
                if (!other.equals(player)) {
                    Coordinate otherPos = other.getRobot().getPosition();
                    if (!pos.equals(otherPos)) {
                        //chosen StartingPoint is valid
                        player.getRobot().setPosition(pos);
                        server.communicateAll(new StartingPointTaken(player.getID(), position));
                    } else player.message(new Error("Your chosen position is already taken!"));
                }
            }
        } else player.message(new Error("This is no valid StartPoint!"));

        //check if all players have set their StartingPoint
        for (Player p : players) {
            if (p.getRobot().getPosition() == null) return;
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
}