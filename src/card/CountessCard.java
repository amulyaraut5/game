package card;

import game.Player;

public class CountessCard extends Card {
    public CountessCard(String name_of_card, int card_value){
        this.name_of_card = name_of_card;
        this.card_value = card_value;

    }

    @Override
    String getCardName() {
        return name_of_card;
    }

    @Override
    int getCardValue() {
        return card_value;
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
