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
    // If a player holds both this card and either the "king" and "Prince",
    // then this card should be played.
    // Other players will know that the playerPlayingCard
    @Override
    void handlecard(Player playerPlayingCard) {

    }


}
