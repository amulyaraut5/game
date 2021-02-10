package game.gameActions;

import game.Player;
import game.gameObjects.cards.Card;
import utilities.enums.CardType;

import java.util.ArrayList;

/**
 * Repeats the programming in the players previous register.
 * This card cannot be played in the first register.
 */
public class AgainAction extends Action {

    /**
     * Repeats the programming in the players previous register.
     * If the previous register was a damage card,
     * it draws a card from the top of the programming deck, and plays that card this register.
     *
     * @param player is the player who is affected by the game action.
     */
    @Override
    public void doAction(Player player) {

        ArrayList<CardType> damageCards = new ArrayList<>();
        damageCards.add(CardType.Spam);
        damageCards.add(CardType.Trojan);
        damageCards.add(CardType.Virus);
        damageCards.add(CardType.Worm);

        for (CardType damageCard : damageCards) {
            if (player.getLastRegisterCard().equals(damageCard)) {
                if (player.getDrawProgrammingDeck().isEmpty()) {
                    player.reuseDiscardedDeck();
                }
                Card topCard = player.getDrawProgrammingDeck().pop();
                game.getActivationPhase().handleCard(topCard.getName(), player);
            }
        }

        game.getActivationPhase().handleCard(player.getLastRegisterCard(), player);
    }
}
