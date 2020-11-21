package card;

import game.Player;

public class BaronCard extends Card {
    public BaronCard(String name_of_card, int card_value){
        this.name_of_card = name_of_card;
        this.card_value = card_value;
    }

    @Override
    String getCardName() {
        return this.name_of_card;
    }

    @Override
    int getCardValue() {
        return card_value;
    }

    @Override
    void handlecard(Player playerPlayingCard) {

    }


}
