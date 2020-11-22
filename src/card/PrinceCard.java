package card;

import game.Player;

public class PrinceCard extends Card {
    private Player targetPlayer = null;
    private String targetPlayerName = "";

    public PrinceCard(String nameOfCard, int cardValue) {
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
     * By calling this method player can choose any player including themselves to discard their card
     * and draw a new card. If the discarded card is princess the player will be eliminated from the round.
     * @param playerPlayingCard
     */
    @Override
    public void handleCard(Player playerPlayingCard) {
        getAvailablePlayers();

        //Display the player name from the availablePlayers so that the player can choose the name
        playerPlayingCard.message("Choose one of these players: " + availablePlayers.toString());

        //Read the input of the user and return the target player
        getTargetPlayer();

        //checks if card of selected player is princess  and kick the player out of the round.
        if (targetPlayer.getCard().getCardName().equals("Princess")) {
            round.kickPlayer(targetPlayer);
            //Display message to all the players    -maybe replace with communicateAll?
            for (Player player : availablePlayers){
                player.message(targetPlayer + " has discarded a princess because of " + playerPlayingCard + "!" +
                            "\nThe player is now out of the game!" +
                            " \n*shakes fist angrily");
            }
            } else {
            //targetPlayer needs to draw a card from the deck
                Card currentCard = targetPlayer.getCard();
                targetPlayer.getPlayedCards().add(currentCard);
                targetPlayer.setCurrentCard(round.pop());
            }
    }

    /**
     * adds all players that are not guarded to the list availablePlayers
     */
    void getAvailablePlayers(){
        for (Player player : round.getActivePlayers()) {
            if (!player.isGuarded())               // must not be guarded and discarding own card is allowed
            {
                availablePlayers.add(player);
            }
        }
    }

    /**
     * reads the input of player and returns the selected player to which the card effect should be applied.
     * @return targetPlayer the selected player
     */
    Player getTargetPlayer(){
        targetPlayerName = round.readResponse();
        for (Player player : availablePlayers) {
            if(player.getName().equals(targetPlayerName)){
                targetPlayer = player;
            }
        }
        return targetPlayer;
    }
}
