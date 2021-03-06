package game;

import game.gameObjects.cards.Card;
import game.gameObjects.decks.DiscardDeck;
import game.gameObjects.decks.ProgrammingDeck;
import game.gameObjects.robot.Robot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.User;
import utilities.JSONProtocol.body.PlayerAdded;
import utilities.enums.CardType;

import java.util.ArrayList;

/**
 * This class extends user to specify different Attributes for a player.
 *
 * @author janau, annika
 */

public class Player extends User {

    private static final Logger logger = LogManager.getLogger();
    /**
     * the players robot
     */
    private final Robot robot;
    private final Game game = Game.getInstance();
    private boolean usingCheats = false;
    private String uniqueName;
    /**
     * contains the chosen Cards for each register
     */
    private ArrayList<Card> registerCards = new ArrayList<>();
    /**
     * Programming deck that cards can be drawn from
     */
    private ProgrammingDeck drawProgrammingDeck = new ProgrammingDeck();
    private DiscardDeck discardedProgrammingDeck = new DiscardDeck();
    /**
     * The cards that the player draws during the Programming Phase
     */
    private ArrayList<Card> drawnProgrammingCards;
    private int checkPointCounter;
    private int energyCubes;

    /**
     * Constructor for a player object on the serverside which is connected with an user.
     * It is possible to <code>message()</code> the player.
     *
     * @param user User to create a player from
     */
    public Player(User user) {
        id = user.getID();
        name = user.getName();
        figure = user.getFigure();
        thread = user.getThread();

        robot = Robot.create(figure, this);

        checkPointCounter = 0;
        energyCubes = 5;
        drawProgrammingDeck = new ProgrammingDeck();
        discardedProgrammingDeck = new DiscardDeck();
        registerCards = new ArrayList<>();
        createRegister();
    }

    /**
     * Constructor to create a player on the clientside.
     * Used as a data structure to store received information about each player which can be displayed on the view.
     *
     * @param message Received PlayerAdded message to create a player from
     */
    public Player(PlayerAdded message) {
        id = message.getID();
        name = message.getName();
        figure = message.getFigure();
        robot = Robot.create(figure, this);
    }

    /**
     * Creates 5 empty registers.
     */
    public void createRegister() {
        registerCards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            registerCards.add(i, null);
        }
    }

    public void resetDecks(){
        checkPointCounter = 0;
        energyCubes = 5;
        drawProgrammingDeck.getDeck().clear();
        discardedProgrammingDeck.getDeck().clear();
        registerCards.clear();
        createRegister();
    }

    /**
     * used in ProgrammingPhase. If the DrawProgrammingDeck is empty the discarded pile gets flipped and shuffled
     * and can be used to draw cards again.
     */
    public void reuseDiscardedDeck() {
        discardedProgrammingDeck.refillProgrammingDeck(drawProgrammingDeck);
    }

    /**
     * Lets you discard an Array of cards to a specified deck
     *
     * @param cards       that should be discarded
     * @param discardDeck deck the cards should be discarded to
     */
    public void discardCards(ArrayList<Card> cards, DiscardDeck discardDeck) {
        for (Card card : cards) {
            discardDeck.addCard(card);
        }
        cards.clear();
    }

    /**
     * saves the assigned Card for the stated register (1 - 5)
     *
     * @param register addressed register
     * @param card     assigned card
     */
    public void setRegisterCards(int register, Card card) {
        int index = register - 1;
        registerCards.set(index, card);
    }

    /**
     * returns the card that is saved to the given register (1-5)
     *
     * @param register that includes the card
     * @return the card in the register
     */
    public Card getRegisterCard(int register) {
        if (!(registerCards.isEmpty())) {
            int index = register - 1;
            return registerCards.get(index);
        } else {
            return null;
        }

    }

    public ArrayList<Card> getRegisterCards() {
        return registerCards;
    }

    public CardType getLastRegisterCard() {
        return getRegisterCard(game.getActivationPhase().getCurrentRegister() - 1).getName();
    }

    public Robot getRobot() {
        return robot;
    }

    public int getCheckPointCounter() {
        return checkPointCounter;
    }

    public void setCheckPointCounter(int checkPointCounter) {
        this.checkPointCounter = checkPointCounter;
    }

    public int getEnergyCubes() {
        return energyCubes;
    }

    public void setEnergyCubes(int energyCubes) {
        this.energyCubes = energyCubes;
    }

    public ArrayList<Card> getDrawnProgrammingCards() {
        return drawnProgrammingCards;
    }

    public void setDrawnProgrammingCards(ArrayList<Card> drawnProgrammingCards) {
        this.drawnProgrammingCards = drawnProgrammingCards;
    }

    public ProgrammingDeck getDrawProgrammingDeck() {
        return drawProgrammingDeck;
    }

    public DiscardDeck getDiscardedProgrammingDeck() {
        return discardedProgrammingDeck;
    }

    public boolean isUsingCheats() {
        return usingCheats;
    }

    public void setUsingCheats(boolean usingCheats) {
        this.usingCheats = usingCheats;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

}