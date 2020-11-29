package card;

import game.Player;

/**
 * This card subclass contains the unique functionality of the Prince Card.
 *
 * @author amulya and vossa
 */
public class PrinceCard extends Card {
    /**
     * Assigns the card its name and its value.
     */
    public PrinceCard() {
        nameOfCard = "Prince";
        cardValue = 5;
    }

    /**
     * Player can choose any player including themselves to discard their card and draw a new card.
     * If the discarded card is Princess the player will be eliminated from the round.
     *
     * @param playerPlayingCard the current player
     */
    @Override
    public void handleCard(Player playerPlayingCard) {
        setAvailablePlayers(playerPlayingCard);

        playerPlayingCard.message("Choose one of these players: " + availablePlayers.toString());
        playerPlayingCard.message("Type '#choose <name>' to choose the player.");
        getTargetPlayer(playerPlayingCard);

        //Checks if card of selected player is princess and kick the player out of the round.
        if (targetPlayer.getCard().getCardName().equals("Princess")) {
            round.kickPlayer(targetPlayer);
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
            controller.communicate( targetPlayer + " has to draw a new card.", targetPlayer);
            targetPlayer.message("You have to draw a new card. Your new card is " + targetPlayer.getCard() + ".");
        }
        availablePlayers.clear();
    }

    /**
     * Exceptional case only for PrinceCard class because a player can target himself to discard the card and choose
     * new one.
     * @param playerPlayingCard the current player
     */
    @Override
    public void setAvailablePlayers(Player playerPlayingCard) {
        for (Player player : round.getActivePlayers()) {
            if (!player.isGuarded()) {
                availablePlayers.add(player);
            }
        }
    }

}
