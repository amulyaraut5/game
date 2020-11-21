package card;

import game.Player;
import java.util.ArrayList;

public class PrinceCard extends Card {
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

        String targetPlayername;
        for (Player player : round.getActivePlayers()) {
            if (!player.isGuarded                // must not be guarded
                    && player != playerPlayingCard)   // Not needed? can be prince (discarding own card is allowed)
            {
                availablePlayers.add(player);
            }
        }
        // TODO Display the player name from the availablePlayers so that the player can choose the name

        playerPlayingCard.message("Choose one of these players: " + availablePlayers.toString());

        // Read the input of the user and set to targetPlayer
        // Set the targetPlayer as per users choice from the list of players
        targetPlayername = gameboard.readResponse();

        for (Player targetPlayer : availablePlayers) {

            // And targetPlayer can be playerPlayingCard which is already included in availablePlayers.
            if (targetPlayer.getName().equals(targetPlayername)){
                if (targetPlayer.getCard().getCardName().equals("princess")) {

                    round.kickPlayer(targetPlayer);
                    //TODO Display message to all the players
                    System.out.println("Target player has discarded a princess because of you! " +
                            "\nTarget player is now out of the game!" +
                            " \n*shakes fist angrily");
                }
                else {
                    // TODO targetPlayer needs to draw a card from the deck
                }

            }
        }
    }
}
