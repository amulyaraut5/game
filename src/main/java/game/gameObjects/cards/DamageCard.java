package game.gameObjects.cards;

import game.Game;
import game.Player;

public abstract class DamageCard extends Card {

    public DamageCard(String cardName) {
        super("Damage Card");
    }

    @Override
    public void handleCard(Game game, Player player) {

    }


}