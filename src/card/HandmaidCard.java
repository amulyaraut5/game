package card;

import game.Player;

/**
 * This card subclass contains the unique functionality of the Handmaid Card.
 *
 * @author amulya and vossa
 */
public class HandmaidCard extends Card {
    /**
     * Assigns the card its name and its value.
     */
    public HandmaidCard() {
        nameOfCard = "Handmaid";
        cardValue = 4;
    }

    /**
     * Player cannot be affected by the other players card until the next turn.
     *
     * @param playerPlayingCard the current player
     */
    @Override
    public void handleCard(Player playerPlayingCard) {
        playerPlayingCard.setGuarded(true);
        playerPlayingCard.message("You are now guarded for this round.");
        controller.communicate(playerPlayingCard + " is now guarded.", playerPlayingCard);
    }
}
