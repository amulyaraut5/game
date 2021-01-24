package game.gameActions;

import game.Player;
import game.gameObjects.cards.Card;
import utilities.enums.CardType;
import utilities.enums.Orientation;

import java.util.ArrayList;

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

        //TODO Rule: This card cannot be played in the first register. - prohibit discarding in the first register?
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
