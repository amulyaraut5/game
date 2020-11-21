package card;

import game.Player;

public class BaronCard extends Card {
    public BaronCard(String name_of_card, int cardValue){
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
    public void handlecard(Player playerPlayingCard) {

        String targetPlayername;
        availablePlayers = round.getActivePlayers();
        /*
        for (Player player : players) {
            if (player.inGame &&           // other player must still be in the game
                    !player.isGuarded)     // and must not be guarded)
            {
                availablePlayers.add(player);
            }
        }*/
        // TODO Display the player name from the availablePlayers so that the player can choose the name
        // TODO Change the println statement
        // Print the name from the ....
        for(Player player : availablePlayers) {
            System.out.println(player.getName());
        }


        playerPlayingCard.message("Choose the name of the player you want to target.");
        // TODO Read the input of the user and set to targetPlayer
        // TODO Set the targetPlayer as per users choice from the list of players
        targetPlayername = gameboard.readResponse();


        for(Player targetPlayer: availablePlayers){

            if (targetPlayer.getName().equals(targetPlayername)){
                // Then playerPlayingCard  see the card of the targetPlayer
                // and compares the hand to see who has the greater card_value

                int targetCardValue = targetPlayer.getCard().getCardValue();

                int playerPlayingCardValue = playerPlayingCard.getCard().getCardValue();

                if(targetCardValue > playerPlayingCardValue){
                    targetPlayer.setInGame(false);
                } else {
                    playerPlayingCard.setInGame(false);
                }
            }
        }
    }
}
