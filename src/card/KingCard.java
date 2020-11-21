package card;

import game.Player;

public class KingCard extends Card {
    public KingCard(String nameOfCard, int cardValue){
        this.nameOfCard = nameOfCard;
        this.cardValue = cardValue;
    }

    @Override
    public String getCardName() {
        return nameOfCard;
    }

    @Override
    public int getCardValue() {
        return cardValue;
    }


    /**
     * By calling this method the playing trades card with another card
     * @param playerPlayingCard
     */
    @Override
    public void handlecard(Player playerPlayingCard) {
        String targetPlayername;
        String printPlayers="";

        for (Player player : round.getActivePlayers()) {
            if(!player.isGuarded && player.getName() != playerPlayingCard.getName()) {
                printPlayers += (player.getName() + " ");
            }
        }
        playerPlayingCard.message(printPlayers);    // Display the player name from the availablePlayers so that the player can choose the name

        playerPlayingCard.message("Choose the name of the player you want to target.");
        // Read the input of the user and set to targetPlayer
        // Set the targetPlayer as per users choice from the list of players
        targetPlayername = gameboard.readResponse();

        for(Player targetPlayer: round.getActivePlayers()){

            if (targetPlayer.getName().equals(targetPlayername)){
                Card temp = targetPlayer.getCard();
                targetPlayer.setCurrentCard(playerPlayingCard.getCard());
                playerPlayingCard.setCurrentCard(temp);
            }
        }
    }


}
