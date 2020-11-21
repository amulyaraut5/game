package card;
import game.Player;
import game.GameBoard;
import java.util.ArrayList;
import game.Round;

public abstract class Card {
    public int cardValue;
    public String nameOfCard;
    GameBoard gameboard;
    Round round;

    ArrayList<Player> availablePlayers = new ArrayList<Player>();



    public abstract String getCardName();
    public abstract int getCardValue();

    abstract void handlecard(Player playerPlayingCard);
}