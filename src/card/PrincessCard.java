package card;

import game.Player;

public class PrincessCard extends Card {
    public PrincessCard(String name_of_card, int card_value) {
        this.name_of_card = name_of_card;
        this.card_value = card_value;

    }

    @Override
    String getCardName() {
        return name_of_card;
    }

    @Override
    public int getCardValue() {
        return card_value;
    }


    /**
     * If a player discard this card, then the player will be out of the round.
     * @param playerPlayingCard
     */
    @Override
    void handlecard(Player playerPlayingCard) {
        playerPlayingCard.setInGame(false);

        //TODO Change the println statement
        // display message to all the players
        System.out.println("You have been eliminated from the round");
    }



}
