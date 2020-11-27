package card;

import game.Player;

/**
 * This card subclass contains the unique functionality of the Princess Card.
 *
 * @author amulya and vossa
 */
public class PrincessCard extends Card {
    public PrincessCard() {
        nameOfCard = "Princess";
        cardValue = 8;

    }

    /**
     * If a player discard this card, then the player will be out of the round.
     *
     * @param playerPlayingCard the current player
     */
    @Override
    public void handleCard(Player playerPlayingCard) {
        round.kickPlayer(playerPlayingCard);
        playerPlayingCard.message("You have been eliminated from the round!");
        controller.communicate(playerPlayingCard + " is eliminated from the round.", playerPlayingCard);
    }
}
