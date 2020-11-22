package card;

import game.GameController;
import game.Player;
import game.Round;

import java.util.ArrayList;

public abstract class Card {
    protected static Round round;

    protected static GameController controller;

    public int cardValue;
    public String nameOfCard;
    public Player targetPlayer;

    ArrayList<Player> availablePlayers = new ArrayList<>();

    public static void setRound(Round round) {
        Card.round = round;
    }

    public static void setController(GameController controller) {
        Card.controller = controller;
    }

    public abstract String getCardName();

    public abstract int getCardValue();

    abstract public void handleCard(Player playerPlayingCard);

    /**
     * reads the input of player and reads the input of player and sets the matching player as target player.
     */
    void getTargetPlayer() {
        String targetPlayerName = round.readResponse();
        for (Player player : availablePlayers) {
            if (player.getName().equals(targetPlayerName)) {
                targetPlayer = player;
            }
        }
    }
}