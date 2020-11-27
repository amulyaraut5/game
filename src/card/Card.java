package card;


import game.GameController;
import game.Player;
import game.Round;

import java.util.ArrayList;

/**
 * This is the abstract parent class from which the various child classes are extended.
 * It contains the various methods to be overridden by its child classes.
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

    public String getCardName() {
        return nameOfCard;
    }

    public int getCardValue() {
        return cardValue;
    }

    abstract public void handleCard(Player playerPlayingCard);

    /**
     * Adds all not guarded players to the availablePlayers list, except current player
     */

    public void setAvailablePlayers(Player playerPlayingCard) {
        for (Player player : round.getActivePlayers()) {
            if (!player.isGuarded() && player != playerPlayingCard) {
                availablePlayers.add(player);
            }
        }
    }

    /**
     * reads the input of player and reads the input of player and sets the matching player as target player.
     */
    public void getTargetPlayer(Player p) {
        boolean playerFound = false;
        boolean first = true;
        while (!playerFound) {
            if (!first) {
                p.message("Please choose a player from the list!");
            }
            String targetPlayerName = round.readResponse();
            for (Player player : availablePlayers) {
                if (player.getName().equals(targetPlayerName)) {
                    targetPlayer = player;
                    playerFound = true;
                    break;
                }
            }
            first = false;
        }

    }

    @Override
    public String toString() {
        return nameOfCard;
    }
}
