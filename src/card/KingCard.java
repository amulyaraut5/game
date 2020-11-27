package card;

import game.Player;

/**
 * This card subclass contains the unique functionality of the King Card.
 *
 * @author amulya and vossa
 */

public class KingCard extends Card {
    public KingCard(String nameOfCard, int cardValue) {
        this.nameOfCard = nameOfCard;
        this.cardValue = cardValue;
    }

    /**
     * By calling this method the current player trades card with another player
     *
     * @param playerPlayingCard the current player
     */
    @Override
    public void handleCard(Player playerPlayingCard) {
        setAvailablePlayers(playerPlayingCard);


        if (availablePlayers.size() <= 0) {
            playerPlayingCard.message("There is no player to choose. Your card is discarded without effect.");
        } else {
            // Display the player name from the availablePlayers so that the player can choose the name
            playerPlayingCard.message("Choose the name of the player you want to swap cards with: " + availablePlayers.toString());
            playerPlayingCard.message("Type '#choose <name>' to choose the player.");
            // Read the input of the user and set to targetPlayer
            // Set the targetPlayer
            getTargetPlayer(playerPlayingCard);

            //swap cards
            Card playerCard = playerPlayingCard.getCard();
            Card targetCard = targetPlayer.getCard();
            targetPlayer.setCurrentCard(playerCard);
            playerPlayingCard.setCurrentCard(targetCard);

            //message to the players
            playerPlayingCard.message("You have swapped cards with " + targetPlayer + ". Your new card is: " + targetCard.getCardName() + ".");
            targetPlayer.message(playerPlayingCard + " swapped cards with you. Your new card is: " + playerCard.getCardName());
        }
        availablePlayers.clear();
    }
}
