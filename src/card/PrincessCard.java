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
     * @param playerPlayingCard
     */
    @Override
    public void handlecard(Player playerPlayingCard) {
        round.kickPlayer(playerPlayingCard);

        //TODO Change the println statement
        // display message to all the players
        System.out.println("You have been eliminated from the round");
    }



}
