package card;
import game.Player;
import game.GameBoard;
import java.util.ArrayList;
import game.Round;

public abstract class Card {
    public int card_value;
    public String name_of_card;
    GameBoard gameboard;
    Round round;

    ArrayList<Player> availablePlayers = new ArrayList<Player>();



    public abstract String getCardName();
    public abstract int getCardValue();

    abstract void handlecard(Player playerPlayingCard);
}