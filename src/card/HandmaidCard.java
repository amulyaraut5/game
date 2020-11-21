package card;

import game.Player;

public class HandmaidCard extends Card {
    public HandmaidCard(String nameOfCard, int cardValue) {

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
     * By calling this method Player cannot be affected by the other players card until the next turn.
     * @param playerPlayingCard
     */
    @Override
    public void handleCard(Player playerPlayingCard) {
        playerPlayingCard.setGuarded(true);
    }


}
