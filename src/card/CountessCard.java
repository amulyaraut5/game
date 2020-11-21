package card;

import game.Player;

public class CountessCard extends Card {
    public CountessCard(String nameOfCard, int cardValue){
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


    //

    /**
     * Already handled in Round class
     * @param playerPlayingCard
     */
    @Override
    void handlecard(Player playerPlayingCard) {

    }


}
