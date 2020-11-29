package card;

import game.Player;

/**
 * This card subclass contains the unique functionality of the Baron Card.
 *
 * @author amulya and vossa
 */
public class BaronCard extends Card {
    /**
     * Assigns the card its name and its value.
     */
    public BaronCard() {
        this.nameOfCard = "Baron";
        this.cardValue = 3;
    }

    /**
     * Player can choose the targetPlayer of his choice and privately compare the cards.
     * The Player with the lower cardValue will be eliminated from the round.
     *
     * @param playerPlayingCard the current player
     */
    @Override
    public void handleCard(Player playerPlayingCard) {
        setAvailablePlayers(playerPlayingCard);

        if (availablePlayers.size() <= 0) {
            playerPlayingCard.message("There is no player to choose. Your card is discarded without effect.");
        } else {
            playerPlayingCard.message("Choose one of these players: " + availablePlayers.toString());
            playerPlayingCard.message("Type '#choose <name>' to choose the player.");

            getTargetPlayer(playerPlayingCard);

            if (!round.isTurnEnded()) {
                // Comparing the hand of two players to see who has the greater cardValue
                int targetCardValue = targetPlayer.getCard().getCardValue();
                int playerCardValue = playerPlayingCard.getCard().getCardValue();

                if (targetCardValue > playerCardValue) {
                    // The one with lower value will be kicked out of the round.
                    round.kickPlayer(playerPlayingCard);
                    playerPlayingCard.message("You were kicked out of the round.");
                    controller.communicate(playerPlayingCard + " has been eliminated.", playerPlayingCard);

                } else if (targetCardValue < playerCardValue) {
                    round.kickPlayer(targetPlayer);

                    targetPlayer.message("You were kicked out of the round.");
                    controller.communicate(targetPlayer + " has been eliminated.", targetPlayer);
                } else {
                    playerPlayingCard.message("Both the players have same card value.");
                }
            }
        }
        availablePlayers.clear();
    }
}
