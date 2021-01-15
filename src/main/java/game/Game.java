package game;

import game.gameObjects.decks.SpamDeck;
import game.gameObjects.decks.TrojanDeck;
import game.gameObjects.decks.VirusDeck;
import game.gameObjects.decks.WormDeck;
import game.gameObjects.maps.*;
import game.gameObjects.robot.Robot;
import game.round.ActivationPhase;
import game.round.Phase;
import game.round.ProgrammingPhase;
import game.round.UpgradePhase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;
import server.User;
import utilities.JSONProtocol.body.ActivePhase;
import utilities.MapConverter;
import utilities.Utilities;

import java.util.ArrayList;
import java.util.HashMap;

import static utilities.Utilities.MAX_PLAYERS;
import static utilities.Utilities.MIN_PLAYERS;

/**
 * This class handles the game itself.
 * It saves all the different assets like decks, players etc. and the game is started from here.
 */

public class Game {
    private static final Logger logger = LogManager.getLogger();
    private static Game instance;

    private Server server = Server.getInstance();
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

    private Phase activePhase;

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
        upgradeShop = new UpgradeShop();
        players = new ArrayList<>(6);

        spamDeck = new SpamDeck();
        virusDeck = new VirusDeck();
        wormDeck = new WormDeck();
        trojanDeck = new TrojanDeck();

        createdGame = false;
        runningGame = false;
    }

    /**
     * This methods starts Roborally.
     */
    public void play() {
        reset();

        ArrayList<User> users = server.getUsers();
        for (User user : users) {
            int figure = user.getFigure();
            players.add(new Player(user, Robot.create(figure)));
        }

        map = MapBuilder.constructMap(new TestBlueprint());
        server.communicateAll(MapConverter.convert(map));

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
     *
     * @param phase
     */
    public void nextPhase(Utilities.Phase phase) {
        int phaseNumber = 0; //TODO Aufbauphase im game oder neue Construction-Phase Klasse?
        switch (phase) {
            case CONSTRUCTION:
                phaseNumber = 0;
                server.communicateAll(new ActivePhase(phase));
                //this.constructionPhase = new ProgrammingPhase(); ?
                break;
            case UPGRADE:
                phaseNumber = 1;
                server.communicateAll(new ActivePhase(phase));
                this.programmingPhase = new ProgrammingPhase();
                break;
            case PROGRAMMING:
                phaseNumber = 2;
                server.communicateAll(new ActivePhase(phase));
                this.activationPhase = new ActivationPhase();
                break;
            case ACTIVATION:
                phaseNumber = 3;
                server.communicateAll(new ActivePhase(phase));
                this.upgradePhase = new UpgradePhase();
                break;
            default:
                //
        }
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

    public Phase getActivePhase() {
        return activePhase;
    }
}