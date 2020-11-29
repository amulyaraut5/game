package card;

import game.Player;

/**
 * This card subclass contains the unique functionality of the Priest Card.
 *
 * @author amulya and vossa
 */
public class PriestCard extends Card {
    /**
     * Assigns the card its name and its value.
     */
    public PriestCard() {
        nameOfCard = "Priest";
        cardValue = 2;
    }

    /**
     * Player is allowed to see the hand of another player of his choice.
     * He needs to choose the player from the set of available players.
     *
     * @param playerPlayingCard the current player
     */
    @Override
    public void handleCard(Player playerPlayingCard) {
        setAvailablePlayers(playerPlayingCard);

        if (availablePlayers.size() <= 0) {
            playerPlayingCard.message("There is no player to choose. Your card is discarded without effect.");

        } else {
            playerPlayingCard.message("Choose the player whose card you wish to look at: " + availablePlayers.toString());
            playerPlayingCard.message("Type '#choose <name>' to choose the player.");
            getTargetPlayer(playerPlayingCard);
            if (!round.isTurnEnded()) {
                // Then playerPlayingCard can look at the card of targetPlayer
                String lookAtCard = targetPlayer.getCard().toString();
                playerPlayingCard.message(targetPlayer + " has the card: " + lookAtCard);
            }
        }
        availablePlayers.clear();
    }
}
