package card;

import game.Player;

public class PriestCard extends Card {
    public PriestCard(String nameOfCard, int cardValue) {
        this.nameOfCard = nameOfCard;
        this.cardValue = cardValue;
    }

    @Override
    public String getCardName() {
        return this.nameOfCard;
    }

    @Override
    public int getCardValue() {
        return this.cardValue;
    }

    /**
     * By calling this method player is allowed to see the hand of other player he chooses.
     * He needs to choose the player from the set of available players
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
            playerPlayingCard.message("Choose the player whose card you wish to look at: " + availablePlayers.toString());
            playerPlayingCard.message("Type '#choose <name>' to choose the player.");
            // Read the input of the user and set to targetPlayer
            // Set the targetPlayer as per users choice from the list of players
            getTargetPlayer();

            // Then playerPlayingCard can look at the card of targetPlayer
            // Get the card of targetPlayer and display this card only to the current player
            String lookAtCard = targetPlayer.getCard().getCardName();
            playerPlayingCard.message(targetPlayer + " has the card: " + lookAtCard);
        }
    }
}
