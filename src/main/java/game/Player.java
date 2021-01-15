package game;

import game.gameObjects.cards.Card;
import game.gameObjects.cards.PermUpgradeCard;
import game.gameObjects.cards.TempUpgradeCard;
import game.gameObjects.decks.DiscardDeck;
import game.gameObjects.decks.ProgrammingDeck;
import game.gameObjects.robot.Robot;
import server.User;

import java.util.ArrayList;

/**
 * This class extends user to specify different Attributes for a player.
 */

public class Player extends User {

    private int currentRegister;
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

    private ArrayList<PermUpgradeCard> installedUpgrades;
    private ArrayList<TempUpgradeCard> tempUpgradeCards;


    public Player(User user, Robot robot) {
        id = user.getId();
        name = user.getName();
        figure = user.getFigure();
        thread = user.getThread();

        this.robot = robot;
        energyCubes = 5;
        drawProgrammingDeck=new ProgrammingDeck();
        discardedProgrammingDeck=new DiscardDeck();
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
        registerCards.add(index, card);
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

    public int getCurrentRegister() {
        return currentRegister;
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