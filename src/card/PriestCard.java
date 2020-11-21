package card;

import game.Player;

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
    void handlecard(Player playerPlayingCard) {

        String targetplayername = null;


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

            if (targetPlayer.getName().equals(targetplayername)){
                // Then playerPlayingCard  can see the card of the targetPlayer

                //TODO change every println statement!!!!!!!!!!!!!!!

                // Get the card of targetPlayer and show only to the player playing card
                String card = targetPlayer.getCard().getCardName();


            }
        }
    }
}
