package card;

import game.Player;

/**
 * This card subclass contains the unique functionality of the King Card.
 *
 * @author amulya and vossa
 */

public class KingCard extends Card {
    /**
     * Assigns the card its name and its value.
     */
    public KingCard() {
        nameOfCard = "King";
        cardValue = 6;
    }

    /**
     * The Current player exchanges card with another player.
     *
     * @param playerPlayingCard the current player
     */
    @Override
    public void handleCard(Player playerPlayingCard) {
        setAvailablePlayers(playerPlayingCard);

        if (availablePlayers.size() <= 0) {
            playerPlayingCard.message("There is no player to choose. Your card is discarded without effect.");
        } else {

            playerPlayingCard.message("Choose the name of the player you want to swap cards with: " + availablePlayers.toString());
            playerPlayingCard.message("Type '#choose <name>' to choose the player.");

            getTargetPlayer(playerPlayingCard);
            if (!round.isTurnEnded()) {
                //Swapping of cards between the players.
                Card playerCard = playerPlayingCard.getCard();
                Card targetCard = targetPlayer.getCard();
                targetPlayer.setCurrentCard(playerCard);
                playerPlayingCard.setCurrentCard(targetCard);

                playerPlayingCard.message("You have swapped cards with " + targetPlayer + ". Your new card is: " + targetCard + ".");
                targetPlayer.message(playerPlayingCard + " swapped cards with you. Your new card is: " + playerCard);
            }
        }
        availablePlayers.clear();
    }
}
