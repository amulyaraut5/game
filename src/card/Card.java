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
    protected static Round round;
    protected static GameController controller;

    protected int cardValue;
    protected String nameOfCard;
    protected Player targetPlayer;
    protected ArrayList<Player> availablePlayers = new ArrayList<>();

    public static void setRound(Round round) {
        Card.round = round;
    }

    public static void setController(GameController controller) {
        Card.controller = controller;
    }

    /**
     * Getter method for the CardName
     * @return upon called returns the respective name of card
     */
    public String getCardName() {
        return nameOfCard;
    }

    /**
     * Getter method for the CardValue
     * @return upon called returns the respective value of card
     */
    public int getCardValue() {
        return cardValue;
    }

    /**
     * Abstract method which is different for every card subclasses
     * @param playerPlayingCard is the player who is playing card
     */
    abstract public void handleCard(Player playerPlayingCard);

    /**
     * Adds all not guarded players to the availablePlayers list, except current player.
     * The case with the PrinceCard is handled separately.
     * @param playerPlayingCard is the player who is playing card
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
     * @param p is the player who is playing card
     */
    public void getTargetPlayer(Player p) {
        while(true){
            String targetPlayerName = round.readResponse();
            for (Player player : availablePlayers) {
                if (player.getName().equals(targetPlayerName)) {
                    targetPlayer = player;
                    return;
                }
            }
            p.message("Please choose a player from the list!");
        }
    }

    /**
     * Returns the name string representing the card object.
     * @return the name of the card
     */
    @Override
    public String toString() {
        return nameOfCard;
    }
}
