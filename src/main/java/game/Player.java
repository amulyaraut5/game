package game;

import game.gameObjects.cards.Card;
import game.gameObjects.cards.PermUpgradeCard;
import game.gameObjects.cards.TempUpgradeCard;
import game.gameObjects.decks.DiscardDeck;
import game.gameObjects.decks.ProgrammingDeck;
import game.gameObjects.robot.Robot;
import server.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class extends user to specify different Attributes for a player.
 */

public class Player extends User {


    /**
     * a Map which connects the register and the related card the user chooses
     */
    private Map<Integer, Card> registerAndCards = new HashMap<>();

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

    private int lastCheckpoint;
    private int checkPointCounter;
    private int energyCubes;
    /**
     * the players robot
     */
    private Robot robot;
    private Game game;

    private ArrayList<PermUpgradeCard> installedUpgrades;
    private ArrayList<TempUpgradeCard> tempUpgradeCards;


    public Player(Robot robot) {
        this.robot = robot;
        energyCubes = 5;
        drawProgrammingDeck.createDeck();
        discardedProgrammingDeck.createDeck();
    }

    /**
     * Creates an empty register.
     *
     * @author annika
     */
    public void createRegister() {
        this.registerCards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            registerCards.add(i, null);
        }
    }

    public ArrayList<Card> getRegisterCards() {
        return registerCards;
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

    /**
     * if a player chose a card in a register it gets saved in a map within the player
     * last time edited: sarah
     *
     * @param register
     * @param card
     */
    public void setRegisterAndCards(int register, Card card) {
        registerAndCards.put(register, card);
    }

    public Map<Integer, Card> getRegisterAndCards() {
        return registerAndCards;
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

    public void setDrawnProgrammingCards(ArrayList<Card> drawnProgrammingCards) {
        this.drawnProgrammingCards = drawnProgrammingCards;
    }

    public ArrayList<Card> getDrawnProgrammingCards() {
        return drawnProgrammingCards;
    }

    public void setDrawProgrammingDeck(ProgrammingDeck drawProgrammingDeck) {
        this.drawProgrammingDeck = drawProgrammingDeck;
    }

    public ProgrammingDeck getDrawProgrammingDeck() {
        return drawProgrammingDeck;
    }

    public DiscardDeck getDiscardedProgrammingDeck() {
        return discardedProgrammingDeck;
    }

    public Game getGame() {
        return game;
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