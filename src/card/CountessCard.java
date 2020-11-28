package card;

import game.Player;

/**
 * This card subclass contains the unique functionality of the Countess Card.
 *
 * @author amulya and vossa
 */
public class CountessCard extends Card {
    public CountessCard() {
        nameOfCard = "Countess";
        cardValue = 7;
    }

    /**
     * Notifies all players except the current player that the countess has been discarded.
     * The Countess takes effect while she is in the hand, which is already handled in Round class.
     *
     * @param playerPlayingCard the current player
     */
    @Override
    public void handleCard(Player playerPlayingCard) {
        controller.communicate(playerPlayingCard + " has discarded the Countess.", playerPlayingCard);
    }
}
