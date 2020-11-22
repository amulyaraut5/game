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
    public void handleCard(Player playerPlayingCard) {

        String targetPlayername;
        availablePlayers = round.getActivePlayers();

        String printPlayers="";
        for(Player player : availablePlayers) {
            if(!player.isGuarded() && player != playerPlayingCard) {
                printPlayers += (player.getName() + " ");
            }
        }
        playerPlayingCard.message(printPlayers);    // Display the player name from the availablePlayers so that the player can choose the name

        playerPlayingCard.message("Choose the name of the player you want to target.");
        // Read the input of the user and set to targetPlayer
        // Set the targetPlayer as per users choice from the list of players
        targetPlayername = round.readResponse();

        for(Player targetPlayer: availablePlayers){

            if (targetPlayer.getName().equals(targetPlayername)){

                // Then playerPlayingCard  can see the card of the targetPlayer
                // Get the card of targetPlayer and display this card only to the player playing card

                String card = targetPlayer.getCard().getCardName();
                playerPlayingCard.message("Your targetPlayer has:" + card);
            }
        }
    }
}
