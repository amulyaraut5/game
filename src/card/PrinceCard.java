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

    // Player can choose any player including themselves to discard their card
    // and draw a new card.If the discarded card is princess the player will be eliminated from the round.

    @Override
    void handlecard(Player playerPlayingCard) {

        for (Player player : players) {
            if (player.inGame &&                        // other player must still be in the game
                    !player.isGuarded &&                    // and must not be guarded
                    (this.name_of_card == "PRINCE" || player != playerPlayingCard)  )   // and must not choose himself, unless for the prince (discarding own card is allowed)
            {
                availablePlayers.add(player);
            }
        }
        // TODO Display the player name from the availablePlayers so that the player can choose the name


        // TODO Read the input of the user

        // TODO Set the targetPlayer as per users choice from the list of players

        for(Player targetPlayer: availablePlayers){
            
        }
    }


}
