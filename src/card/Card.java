package card;
import game.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Card {
    public int card_value;
    public String name_of_card;

    Set<Player> availablePlayers = new HashSet<Player>();
    List<Player> players;


    abstract String getCardName();
    abstract int getCardValue();

    abstract void handlecard(Player playerPlayingCard);
}