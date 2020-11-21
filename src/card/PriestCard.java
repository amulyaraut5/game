package card;

public class PriestCard extends Card{
    public PriestCard(String name_of_card, int card_value){
        this.name_of_card = name_of_card;
        this.card_value = card_value;
    }



    @Override
    String getCardName() {
        return this.name_of_card;
    }

    @Override
    int getCardValue() {
        return this.card_value;
    }
    //Player is allowed to see the hand of other player he chooses
    //He needs to choose the player from the set of available players
    @Override
    void handlecard() {

    }
}
