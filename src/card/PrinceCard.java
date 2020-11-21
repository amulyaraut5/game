package card;

import game.Player;

public class PrinceCard extends Card {
    public PrinceCard(String name_of_card, int card_value){
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

    @Override
    void handlecard(Player playerPlayingCard) {

    }


}
