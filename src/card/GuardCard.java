package card;

import game.Player;

public class GuardCard extends Card{
    public GuardCard(String nameOfCard, int cardValue) {

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
     * By calling this method player  designates another player and names a type of card. If that players has that card
     * then the player will be out of the round.
     * However a player cannot name GUARD card.
     * @param playerPlayingCard
     */
    @Override
    public void handlecard(Player playerPlayingCard) {

        String targetPlayername;
        availablePlayers = round.getActivePlayers();

        String printPlayers="";
        for(Player player : availablePlayers) {
            if(!player.isGuarded && player != playerPlayingCard) {
                printPlayers += (player.getName() + " ");
            }
        }
        playerPlayingCard.message(printPlayers);   // Display the player name from the availablePlayers so that the player can choose the name

        // Read the input of the user and set to targetPlayer
        // Set the targetPlayer as per users choice from the list of players
        targetPlayername = gameboard.readResponse();

        for(Player targetPlayer: availablePlayers){

            if (targetPlayer.getName().equals(targetPlayername)){

                // Then playerPlayingCard  can guess the card of the targetPlayer
                playerPlayingCard.message("What card do you think the target player has?");
                // Read the input of the player
                String guessCardName = gameboard.readResponse();

                if (guessCardName == this.nameOfCard ) {
                    playerPlayingCard.message("You cannot choose the guard name");

                }else if(guessCardName.equals(targetPlayer.getCard().getCardName())) {
                    playerPlayingCard.message("Your guess was correct");

                    //If the guess is correct the player will be out of the round.
                    round.kickPlayer(targetPlayer);
                    //TODO Display message to all the players

                }else {
                    playerPlayingCard.message("Your guess was Incorrect.");
                }

            }
        }

    }
}
