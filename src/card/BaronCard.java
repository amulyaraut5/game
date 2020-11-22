package card;

import game.Player;
import game.Round;

public class BaronCard extends Card {
    public BaronCard(String nameOfCard, int cardValue){
        this.nameOfCard = nameOfCard;
        this.cardValue = cardValue;
    }

    @Override
    public String getCardName() {
        return this.nameOfCard;
    }

    @Override
    public int getCardValue() {
        return cardValue;
    }

    /**
     * By calling this method player will choose the targetPlayer and privately compare the cards.
     * The Player with the lower card_value will be eliminated from the round.
     * @param playerPlayingCard the current player
     */
    @Override
    public void handleCard(Player playerPlayingCard) {
        setAvailablePlayers(playerPlayingCard);


        if (availablePlayers.size() <= 0) {
            playerPlayingCard.message("There is no player to choose. Your card is discarded without effect.");
        } else {
            // Display the availablePlayers so that the player can choose one
            playerPlayingCard.message("Choose one of these players: " + availablePlayers.toString());
            playerPlayingCard.message("Type #choose <name> to choose the player.");
            // Read the input of the user and return the target player
            getTargetPlayer();
            // compares the hand to see who has the greater cardValue
            int targetCardValue = targetPlayer.getCard().getCardValue();
            int playerCardValue = playerPlayingCard.getCard().getCardValue();

            if (targetCardValue > playerCardValue) {
                round.kickPlayer(playerPlayingCard);
                //Display message to all the players
                playerPlayingCard.message("You were kicked out of the round.");
                controller.communicate(targetPlayer + " has been eliminated.", playerPlayingCard);
            } else if (targetCardValue < playerCardValue) {
                round.kickPlayer(targetPlayer);
                //Display message to all the players
                playerPlayingCard.message("You were kicked out of the round.");
                controller.communicate(targetPlayer + " has been eliminated.", playerPlayingCard);
            } else {
                playerPlayingCard.message("Both the players have same card value.");
            }
        }
    }
}
