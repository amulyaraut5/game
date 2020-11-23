package card;

import game.Player;

/**
 * This card subclass contains the unique functionality of the Guard Card.
 * @author amulya and vossa
 */
public class GuardCard extends Card {
    public GuardCard(String nameOfCard, int cardValue) {
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
     * By calling this method player designates another player and names a type of card. If that players has that card
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
            // Display the player name from the availablePlayers so that the player can choose the name
            playerPlayingCard.message("Choose one of these players: " + availablePlayers.toString());
            playerPlayingCard.message("Type '#choose <name>' to choose the player.");
            // Read the input of the user and set to targetPlayer
            // Set the targetPlayer as per users choice from the list of players
            getTargetPlayer();


            while (true) {
                // Then playerPlayingCard can guess the card of the targetPlayer
                playerPlayingCard.message("Which card do you think the player has? Type '#choose <card>'");
                playerPlayingCard.message("Choose one Card: [Priest, Baron, Handmaid, Prince, King, Countess, Princess]");
                // Read the input of the player
                String guessCardName = round.readResponse();
                if (guessCardName.equals(this.nameOfCard)) {
                    playerPlayingCard.message("You cannot choose the Guard card, try again.");


                } else if (guessCardName.equals(targetPlayer.getCard().getCardName())) {
                    playerPlayingCard.message("Your guess was correct!");
                    //If the guess is correct the player will be out of the round.
                    round.kickPlayer(targetPlayer);
                    //Display message to all the players
                    controller.communicateAll(targetPlayer + " is eliminated from the round.");
                    break;
                } else {
                    playerPlayingCard.message("Your guess was Incorrect.");
                    break;
                }

            }

        }
    }
}
