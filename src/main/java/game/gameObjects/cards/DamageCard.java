package game.gameObjects.cards;

import game.Game;
import game.Player;
import utilities.Utilities.CardType;

public abstract class DamageCard extends ProgrammingCard {

    public DamageCard(CardType card) {
        super(card);
    }

    @Override
    public void handleCard(Game game, Player player) {

    }


}