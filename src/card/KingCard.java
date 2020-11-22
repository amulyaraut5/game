package card;

import game.Player;

public class KingCard extends Card {
    public KingCard(String nameOfCard, int cardValue){
        this.nameOfCard = nameOfCard;
        this.cardValue = cardValue;
    }

    @Override
    public String getCardName() {
        return nameOfCard;
    }

    @Override
    public int getCardValue() {
        return cardValue;
    }


    /**
     * By calling this method the current player trades card with another player
     * @param playerPlayingCard the current player
     */
    @Override
    public void handleCard(Player playerPlayingCard) {

        for (Player player : round.getActivePlayers()) {
            if (!player.isGuarded() && player != playerPlayingCard) {
                availablePlayers.add(player);

            }
        }

        if (availablePlayers.size() <= 0) {
            playerPlayingCard.message("There is no player to choose. Your card is discarded without effect.");
        } else {
            // Display the player name from the availablePlayers so that the player can choose the name
            playerPlayingCard.message("Choose the name of the player you want to swap cards with: " + availablePlayers.toString());
            // Read the input of the user and set to targetPlayer
            // Set the targetPlayer as per users choice from the list of players
            getTargetPlayer();

            //swap cards
            Card playerCard = playerPlayingCard.getCard();
            Card targetCard = targetPlayer.getCard();
            targetPlayer.setCurrentCard(playerCard);
            playerPlayingCard.setCurrentCard(targetCard);
            //TODO message to the players

        }
    }
}
