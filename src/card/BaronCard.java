package card;

import game.Player;

public class BaronCard extends Card {
    public BaronCard(String nameOfCard, int cardValue){
        this.nameOfCard = nameOfCard;
        this.cardValue = cardValue;
    }

    @Override
    public String getCardName() {
        return this.nameOfCard;
    }

    @Override
    public int getCardValue() {
        return cardValue;
    }

    /**
     * By calling this method player will choose the targetPlayer and privately compare the cards.
     * The Player with the lower card_value will be eliminated from the round.
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
        targetPlayername = gameboard.readResponse();

        for(Player targetPlayer: availablePlayers){

            if (targetPlayer.getName().equals(targetPlayername)){
                // Then playerPlayingCard  see the card of the targetPlayer
                // and compares the hand to see who has the greater card_value

                int targetCardValue = targetPlayer.getCard().getCardValue();

                int playerPlayingCardValue = playerPlayingCard.getCard().getCardValue();

                if(targetCardValue > playerPlayingCardValue){
                    round.kickPlayer(playerPlayingCard);
                    //TODO Display message to all the players
                } else {
                    round.kickPlayer(targetPlayer);
                    //TODO Display message to all the players
                }
            }
        }
    }
}
