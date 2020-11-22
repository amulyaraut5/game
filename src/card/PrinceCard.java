package card;

import game.Player;
import game.Round;

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
     * @param playerPlayingCard the current player
     */
    @Override
    public void handleCard(Player playerPlayingCard) {
        setAvailablePlayers();

        //Display the player name from the availablePlayers so that the player can choose the name
        playerPlayingCard.message("Choose one of these players: " + availablePlayers.toString());

        //Read the input of the user and return the target player
        getTargetPlayer();

        //checks if card of selected player is princess  and kick the player out of the round.
        if (targetPlayer.getCard().getCardName().equals("Princess")) {
            round.kickPlayer(targetPlayer);
            //Display message to all the players
            controller.communicateAll(targetPlayer + " has discarded a princess because of " + playerPlayingCard + "!" +
                            "\nThe player is now out of the game!" +
                            " \n*shakes fist angrily");
            } else {
            //targetPlayer needs to draw a card from the deck
                Card currentCard = targetPlayer.getCard();
                targetPlayer.getPlayedCards().add(currentCard);
                targetPlayer.setCurrentCard(round.pop());
            }
    }

    @Override
    /**
     * adds all players that are not guarded to the list availablePlayers
     */
    void setAvailablePlayers() {
        for (Player player : round.getActivePlayers()) {
            if (!player.isGuarded()) {  // must not be guarded and discarding own card is allowed
                availablePlayers.add(player);
            }
        }
    }
}
