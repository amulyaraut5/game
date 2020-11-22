package card;
import game.Player;
import game.GameBoard;
import java.util.ArrayList;
import game.Round;
import game.GameController;

public abstract class Card {
    public int cardValue;
    public String nameOfCard;
    GameBoard gameboard;
    Round round;
    GameController controller;

    ArrayList<Player> availablePlayers;

    public abstract String getCardName();
    public abstract int getCardValue();

    abstract public void handleCard(Player playerPlayingCard);
}