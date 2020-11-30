package card;


import game.GameController;
import game.Player;
import game.Round;

import java.util.ArrayList;

/**
 * This is the abstract parent class from which the various child classes are extended.
 * It mainly contains the handleCard method to be overridden by its child classes.
 *
 * @author amulya and vossa
 */
public abstract class Card {
    /**
     * Class variable of round.
     */
    protected static Round round;
    /**
     * Class variable of the GameController.
     */
    protected static GameController controller;
    /**
     * The value of the card.
     */
    protected int cardValue;
    /**
     * The name of the card.
     */
    protected String nameOfCard;
    /**
     * The player selected by the current player.
     */
    protected Player targetPlayer;
    /**
     * The list of players that the current player can select to apply a card effect.
     */
    protected ArrayList<Player> availablePlayers = new ArrayList<>();


    /**
     * Sets the round.
     *
     * @param round the game round
     */
    public static void setRound(Round round) {
        Card.round = round;
    }

    /**
     * Sets the GameController.
     *
     * @param controller the GameController
     */
    public static void setController(GameController controller) {
        Card.controller = controller;
    }

    /**
     * Gets the card name.
     *
     * @return upon called returns the respective name of card
     */
    public String getCardName() {
        return nameOfCard;
    }

    /**
     * Gets the value of the card.
     *
     * @return upon called returns the respective value of card
     */
    public int getCardValue() {
        return cardValue;
    }

    /**
     * Abstract method which is different for every card subclasses.
     * In the subclasses it handles the features of each individual card.
     *
     * @param playerPlayingCard current player
     */
    abstract public void handleCard(Player playerPlayingCard);

    /**
     * Adds all unguarded players to the availablePlayers list, except the current player.
     * The case with the PrinceCard is handled separately.
     *
     * @param playerPlayingCard current player
     */
    public void setAvailablePlayers(Player playerPlayingCard) {
        for (Player player : round.getActivePlayers()) {
            if (!player.isGuarded() && player != playerPlayingCard) {
                availablePlayers.add(player);
            }
        }
    }

    /**
     * Reads the input of player and sets the matching player as target player.
     *
     * @param p is the current player who is playing card
     */
    public void getTargetPlayer(Player p) {
        while (true) {
            String targetPlayerName = round.readResponse();
            if (!round.isTurnEnded()) {
                for (Player player : availablePlayers) {
                    if (player.getName().equals(targetPlayerName)) {
                        targetPlayer = player;
                        return;
                    }
                }
                p.message("Please choose a player from the list!");
            } else {
                return;
            }
        }
    }

    /**
     * Returns the name string representing the card object.
     *
     * @return the name of the card
     */
    @Override
    public String toString() {
        return nameOfCard;
    }
}
