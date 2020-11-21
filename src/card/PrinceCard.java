package card;

import game.Player;

public class PrinceCard extends Card {
    Player targetPlayer = null;
    String targetPlayerName = "";
    Player playerPlayingCard = null;

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
    public void handlecard(Player playerPlayingCard) {

        for (Player player : round.getActivePlayers()) {
            if (!player.isGuarded)               // must not be guarded
            {
                availablePlayers.add(player);
            }
        }
        // TODO Display the player name from the availablePlayers so that the player can choose the name
        // TODO Change the println statement
        // Print the name from the Set<Player>....
        playerPlayingCard.message("Choose one of these players: " + availablePlayers.toString());

        //Read the input of the user and return the target player
        getTargetPlayer();

        // TODO Set the targetPlayer as per users choice from the list of players

        for (Player targetPlayer : availablePlayers) {
            // And targetPlayer can be playerPlayingCard which is already included in availablePlayers.
            // TODO Change the println statement

            if (targetPlayer.getCard().getCardName().equals("Princess")) {
                // Display this messages to all the players.

                System.out.println("Target player has discarded a princess because of " + playerPlayingCard + "!" +
                        "\nTarget player is now out of the game!" +
                        " \n*shakes fist angrily");
                //we kick the player out of the round.
                round.kickPlayer(targetPlayer);
            }
            else{
                targetPlayer.setCurrentCard(round.pop());
                // TODO targetPlayer needs to draw a card from the deck

            }
        }

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
