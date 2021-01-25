package game;

import game.gameObjects.cards.Card;
import game.gameObjects.decks.DiscardDeck;
import game.gameObjects.decks.ProgrammingDeck;
import game.gameObjects.robot.Robot;
import server.User;
import utilities.JSONProtocol.body.PlayerAdded;
import utilities.enums.CardType;

import java.util.ArrayList;

/**
 * This class extends user to specify different Attributes for a player.
 */

public class Player extends User {

    protected Game game = Game.getInstance();

    /**
     * contains the chosen Cards for each register
     */
    private ArrayList<Card> registerCards;

    /**
     * Programming deck that cards can be drawn from
     */
    private ProgrammingDeck drawProgrammingDeck;
    private DiscardDeck discardedProgrammingDeck;
    /**
     * The cards that the player draws during the Programming Phase
     */
    private ArrayList<Card> drawnProgrammingCards;

    private Card currentAction;
    private Card lastAction;

    private int checkPointCounter;
    private int energyCubes;
    /**
     * the players robot
     */
    private Robot robot;


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

        robot = Robot.create(figure);

        energyCubes = 5;
        drawProgrammingDeck = new ProgrammingDeck();
        discardedProgrammingDeck = new DiscardDeck();
        registerCards = new ArrayList<Card>();
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
        robot = Robot.create(figure);
    }

    /**
     * Creates 5 empty registers.
     *
     * @author annika
     */
    public void createRegister() {
        this.registerCards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            registerCards.add(i, null);
        }
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

    public ArrayList<Card> getRegisterCards() {
        return registerCards;
    }

    /**
     * returns the card that is saved to the given register (1-5)
     *
     * @param register
     * @return
     */
    public Card getRegisterCard(int register) {
        int index = register - 1;
        return registerCards.get(index);
    }

    public CardType getLastRegisterCard(){
        return getRegisterCard(game.getActivationPhase().getCurrentRegister()-1).getName();
    }

    public Card getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(Card currentAction) {
        this.currentAction = currentAction;
    }

    public Card getLastAction() {
        return lastAction;
    }

    public void setLastAction(Card lastAction) {
        this.lastAction = lastAction;
    }

    public Robot getRobot() {
        return this.robot;
    }

    public int getCheckPointCounter() {
        return checkPointCounter;
    }

    public void setCheckPointCounter(int checkPointCounter) {
        this.checkPointCounter = checkPointCounter;
    }

    public void addEnergyCubes(int n) {
        this.energyCubes = this.energyCubes + n;
    }

    public void takeEnergyCubes(int n) {
        this.energyCubes = this.energyCubes - n;
    }

    public void checkPointReached() {
        this.checkPointCounter++;
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

    public void setDrawProgrammingDeck(ProgrammingDeck drawProgrammingDeck) {
        this.drawProgrammingDeck = drawProgrammingDeck;
    }

    public DiscardDeck getDiscardedProgrammingDeck() {
        return discardedProgrammingDeck;
    }

    /**
     * Lets you discard an Array of cards to a specified deck
     *
     * @param cards
     * @param discardDeck
     */
    public void discardCards(ArrayList<Card> cards, DiscardDeck discardDeck) {
        for (Card card : cards) {
            discardDeck.addCard(card);
        }
        cards.clear();
    }

    /**
     * It freezes the player from the current round.
     * Effect of Pit or falling off from  the map
     */
    public void freeze() {
    }

    /**
     * used in ProgrammingPhase. If the DrawProgrammingDeck is empty the discarded pile gets flipped and shuffled
     * and can be used to draw cards again.
     */
    public void reuseDiscardedDeck() {
        discardedProgrammingDeck.refillProgrammingDeck(drawProgrammingDeck);
    }

}