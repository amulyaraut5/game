package card;

import game.Player;

public class BaronCard extends Card {
    private Player targetPlayer = null;

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
     * @param playerPlayingCard
     */
    @Override
    public void handleCard(Player playerPlayingCard) {
        for (Player player : round.getActivePlayers()) {
            if (!player.isGuarded() && player != playerPlayingCard) {
                availablePlayers.add(player);
            }
        }

        // Display the availablePlayers so that the player can choose one
        playerPlayingCard.message("Choose one of these players: " + availablePlayers.toString());
        // Read the input of the user and return the target player
        getTargetPlayer();
        // compares the hand to see who has the greater cardValue
        int targetCardValue = targetPlayer.getCard().getCardValue();
        int playerCardValue = playerPlayingCard.getCard().getCardValue();

        if (targetCardValue > playerCardValue) {
            round.kickPlayer(playerPlayingCard);
            //TODO Display message to all the players
            for (Player player : availablePlayers) {
                player.message(playerPlayingCard + " has been eliminated.");
            }
        } else if (targetCardValue < playerCardValue) {
            round.kickPlayer(targetPlayer);
            //TODO Display message to all the players
            for (Player player : availablePlayers) {
                player.message(targetPlayer + " has been eliminated.");
            }
        } else {
            playerPlayingCard.message("Both the players have same card value.");
            }
    }
    /**
     * reads the input of player and returns the selected player to which the card effect should be applied.
     * @return targetPlayer the selected player
     */
    Player getTargetPlayer(){
        String targetPlayerName = round.readResponse();
        for (Player player : availablePlayers) {
            if(player.getName().equals(targetPlayerName)){
                targetPlayer = player;
            }
        }
        return targetPlayer;
    }
}
