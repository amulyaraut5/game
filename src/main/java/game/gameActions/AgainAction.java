package game.gameActions;

import game.Player;
import game.gameObjects.cards.Card;
import game.gameObjects.cards.damage.Spam;
import game.gameObjects.cards.damage.Trojan;
import game.gameObjects.cards.damage.Virus;
import game.gameObjects.cards.damage.Worm;
import utilities.Orientation;

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
        Spam spam = new Spam();
        Trojan trojan = new Trojan();
        Virus virus = new Virus();
        Worm worm = new Worm();

        List<Card> damageCards = Arrays.asList(spam, trojan, virus, worm);

        for (Card damageCard : damageCards) {
            if (player.getLastAction() == damageCard) {
                //draw top card from programming deck and play it
                Card topCard = player.getDrawProgrammingDeck().pop();
                topCard.handleCard(player.getGame(), player);
            }
        }
        //player.getLastAction().handleCard(player.getGame(), player);
        player.setCurrentAction(player.getLastAction());

        //If you used an upgrade in your previous register that allowed you to play multiple programming cards,
        // re-execute the second card
    }
}
