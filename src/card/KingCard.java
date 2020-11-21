package card;

import game.Player;

public class KingCard extends Card {
    private Player targetPlayer = null;
    private String targetPlayerName = "";

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
     * By calling this method the current player trades card with another player
     * @param playerPlayingCard
     */
    @Override
    public void handlecard(Player playerPlayingCard) {

        for (Player player : round.getActivePlayers()) {
            if(!player.isGuarded && player != playerPlayingCard) {
                availablePlayers.add(player);
            }
        }
        // Display the player name from the availablePlayers so that the player can choose the name
        playerPlayingCard.message("Choose the name of the player you want to target." + availablePlayers.toString());
        // Read the input of the user and set to targetPlayer
        // Set the targetPlayer as per users choice from the list of players
        getTargetPlayer();

        //swap cards
        Card playerCard = playerPlayingCard.getCard();
        Card targetCard = targetPlayer.getCard();
        targetPlayer.setCurrentCard(playerCard);
        playerPlayingCard.setCurrentCard(targetCard);
    }

    /**
     * reads the input of player and returns the selected player to which the card effect should be applied.
     * @return targetPlayer the selected player
     */
    Player getTargetPlayer(){
        targetPlayerName = gameboard.readResponse();
        for (Player player : availablePlayers) {
            if(player.getName().equals(targetPlayerName)){
                targetPlayer = player;
            }
        }
        return targetPlayer;
    }

}
