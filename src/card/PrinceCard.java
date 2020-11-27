package card;

import game.Player;

/**
 * This card subclass contains the unique functionality of the Prince Card.
 *
 * @author amulya and vossa
 */
public class PrinceCard extends Card {
    public PrinceCard(String nameOfCard, int cardValue) {
        this.nameOfCard = nameOfCard;
        this.cardValue = cardValue;
    }

    /**
     * By calling this method player can choose any player including themselves to discard their card
     * and draw a new card. If the discarded card is princess the player will be eliminated from the round.
     *
     * @param playerPlayingCard the current player
     */
    @Override
    public void handleCard(Player playerPlayingCard) {
        setAvailablePlayers(playerPlayingCard);

        //Display the player name from the availablePlayers so that the player can choose the name
        playerPlayingCard.message("Choose one of these players: " + availablePlayers.toString());
        playerPlayingCard.message("Type '#choose <name>' to choose the player.");
        //Read the input of the user and return the target player
        getTargetPlayer(playerPlayingCard);

        //checks if card of selected player is princess and kick the player out of the round.
        if (targetPlayer.getCard().getCardName().equals("Princess")) {
            round.kickPlayer(targetPlayer);
            //Display message to all the players
            controller.communicateAll(targetPlayer + " has discarded a princess because of " + playerPlayingCard + "!\n" +
                    "The player is now out of the game!\n" +
                    "*" + targetPlayer + " shakes fist angrily*");
        } else if (round.getCardDeck().size() <= 0) {
            // If the deck is empty, targetPlayer should pick the card which was removed at the beginning of play.
            Card currentCard = targetPlayer.getCard();
            targetPlayer.getPlayedCards().add(currentCard);
            targetPlayer.setCurrentCard(round.getFirstCardRemoved());
        } else {
            //targetPlayer needs to draw a card from the deck
            Card currentCard = targetPlayer.getCard();
            targetPlayer.getPlayedCards().add(currentCard);
            targetPlayer.setCurrentCard(round.pop());
        }
        availablePlayers.clear();
    }

    @Override
    /**
     * adds all players that are not guarded to the list availablePlayers
     */
    public void setAvailablePlayers(Player playerPlayingCard) {
        for (Player player : round.getActivePlayers()) {
            if (!player.isGuarded()) {  // must not be guarded and discarding own card is allowed
                availablePlayers.add(player);
            }
        }
    }

}
