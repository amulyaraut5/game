package card;

import game.Player;

public class PriestCard extends Card{
    public PriestCard(String nameOfCard, int cardValue){
        this.nameOfCard = nameOfCard;
        this.cardValue = cardValue;
    }



    @Override
    public String getCardName() {
        return this.nameOfCard;
    }

    @Override
    public int getCardValue() {
        return this.cardValue;
    }

    /**
     * By calling this method player is allowed to see the hand of other player he chooses.
     * He needs to choose the player from the set of available players
     * @param playerPlayingCard
     */
    @Override
    void handlecard(Player playerPlayingCard) {

        String targetplayername = null;


        for (Player player : round.getActivePlayers()) {
            if (!player.isGuarded)                  // other player must not be guarded
            {
                availablePlayers.add(player);
            }
        }

        // TODO Display the player name from the availablePlayers so that the player can choose the name
        // TODO Change the println statement
        // Print the name from the Set<Player>....
        for(Player player : availablePlayers) {
            System.out.println(player.getName());
        }

        // TODO Read the input of the user

        // TODO Set the targetPlayer as per users choice from the list of players


        for(Player targetPlayer: availablePlayers){

            if (targetPlayer.getName().equals(targetplayername)){
                // Then playerPlayingCard  can see the card of the targetPlayer

                //TODO change every println statement!!!!!!!!!!!!!!!

                // Get the card of targetPlayer
                // TODO display this card only to the player playing card
                String card = targetPlayer.getCard().getCardName();


            }
        }
    }
}
