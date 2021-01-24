package game.gameActions;

import game.Game;
import game.Player;
import game.gameObjects.cards.Card;
import game.gameObjects.cards.damage.Spam;
import game.gameObjects.cards.damage.Trojan;
import game.gameObjects.cards.damage.Virus;
import game.gameObjects.cards.damage.Worm;
import utilities.enums.CardType;
import utilities.enums.Orientation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Repeats the programming in the players previous register.
 * This card cannot be played in the first register. TODO
 */
public class AgainAction extends Action{

    /**
     * Repeats the programming in the players previous register.
     * If the previous register was a damage card,
     * it draws a card from the top of the programming deck, and plays that card this register.
     * @param orientation
     * @param player is the player who is affected by the gameaction.
     */
    @Override
    public void doAction(Orientation orientation, Player player) {

        ArrayList<CardType> damageCards = new ArrayList<>();
        damageCards.add(CardType.Spam);
        damageCards.add(CardType.Trojan);
        damageCards.add(CardType.Virus);
        damageCards.add(CardType.Worm);

        for (CardType damageCard : damageCards) {
            if (player.getLastRegisterCard() == damageCard) {
                //draw top card from programming deck and play it
                Card topCard = player.getDrawProgrammingDeck().pop();
                game.getActivationPhase().handleCard(topCard.getName(), player);
            }
        }
        game.getActivationPhase().handleCard(player.getLastRegisterCard(), player);
    }
}
