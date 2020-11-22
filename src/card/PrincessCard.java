package card;

import game.Player;

public class PrincessCard extends Card {
    public PrincessCard(String nameOfCard, int cardValue) {
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
     * If a player discard this card, then the player will be out of the round.
     * @param playerPlayingCard the current player
     */
    @Override
    public void handleCard(Player playerPlayingCard) {
        round.kickPlayer(playerPlayingCard);
        playerPlayingCard.message("You have been eliminated from the round");

        //TODO Change the println statement to display message to all the players
        for (Player player : availablePlayers){
            player.message(playerPlayingCard + " is eliminated from the round.");
        }
    }



}
