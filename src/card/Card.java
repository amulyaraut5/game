package card;
import game.Player;
import game.GameBoard;
import java.util.ArrayList;
import game.Round;
import game.GameController;

public abstract class Card {
    public int cardValue;
    public String nameOfCard;
    public Player targetPlayer;
    GameBoard gameboard;
    Round round;
    GameController controller;

    ArrayList<Player> availablePlayers;

    public abstract String getCardName();
    public abstract int getCardValue();

    abstract public void handleCard(Player playerPlayingCard);

    /**
     * reads the input of player and reads the input of player and sets the matching player as target player.
     */
    void getTargetPlayer(){
        String targetPlayerName = round.readResponse();
        for (Player player : availablePlayers) {
            if(player.getName().equals(targetPlayerName)){
                targetPlayer = player;
            }
        }
    }
}