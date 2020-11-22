package card;

import game.Player;

public class PriestCard extends Card{
    private Player targetPlayer = null;
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
     * @param playerPlayingCard the current player
     */
    @Override
    public void handleCard(Player playerPlayingCard) {
        for (Player player : round.getActivePlayers()) {
            if(!player.isGuarded() && player != playerPlayingCard) {
                availablePlayers.add(player);
            }
        }
        // Display the player name from the availablePlayers so that the player can choose the name
        playerPlayingCard.message("Choose the player whose card you wish to look at: " +availablePlayers.toString());
        // Read the input of the user and set to targetPlayer
        // Set the targetPlayer as per users choice from the list of players
        getTargetPlayer();

        // Then playerPlayingCard can look at the card of targetPlayer
        // Get the card of targetPlayer and display this card only to the current player
        String lookAtCard = targetPlayer.getCard().getCardName();
        playerPlayingCard.message(targetPlayer + "has the card: " + lookAtCard);
    }

    /**
     * reads the input of player and sets the matching player as target player.
     */
    void getTargetPlayer(){
        String targetPlayerName = round.readResponse();
        for (Player player : availablePlayers) {
            if(player.getName().equals(targetPlayerName)){
                targetPlayer = player;
            }
        }
    }
}
