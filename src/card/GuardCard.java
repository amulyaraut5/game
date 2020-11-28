package card;

import game.Player;

import java.util.Arrays;

/**
 * This card subclass contains the unique functionality of the Guard Card.
 *
 * @author amulya and vossa
 */
public class GuardCard extends Card {
    public GuardCard() {
        this.nameOfCard = "Guard";
        this.cardValue = 1;
    }

    /**
     * Player designates another player of his choice and names a type of card. If that players has that card
     * then the player will be out of the round.
     * However a player cannot name GUARD card.
     *
     * @param playerPlayingCard the current player
     */
    @Override
    public void handleCard(Player playerPlayingCard) {
        setAvailablePlayers(playerPlayingCard);

        if (availablePlayers.size() <= 0) {
            playerPlayingCard.message("There is no player to choose. Your card is discarded without effect.");
        } else {
            playerPlayingCard.message("Choose one of these players: " + availablePlayers.toString());
            playerPlayingCard.message("Type '#choose <name>' to choose the player.");
            getTargetPlayer(playerPlayingCard);

            while (true) {
                String[] cardNames = {"Priest", "Baron", "Handmaid", "Prince", "King", "Countess", "Princess"};

                playerPlayingCard.message("Which card do you think the player has? Type '#choose <card>'.");
                playerPlayingCard.message("Choose one Card: [Priest, Baron, Handmaid, Prince, King, Countess, Princess]");

                String guessCardName = round.readResponse();
                if (guessCardName.equals(this.nameOfCard)) {
                    playerPlayingCard.message("You cannot choose the Guard card, try again.");
                } else if (Arrays.asList(cardNames).contains(guessCardName)) {
                    if (guessCardName.equals(targetPlayer.getCard().toString())) {
                        playerPlayingCard.message("Your guess was correct!");
                        //If the guess is correct the player will be out of the round.
                        round.kickPlayer(targetPlayer);

                        controller.communicateAll(targetPlayer + " is eliminated from the round.");
                        break;
                    } else {
                        playerPlayingCard.message("Your guess was Incorrect.");
                        break;
                    }
                } else {
                    playerPlayingCard.message("Your might have misstyped the card name. Please try again!");
                }
            }
        }
        availablePlayers.clear();
    }
}
