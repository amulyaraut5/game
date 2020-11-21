package card;

import game.Player;

public class GuardCard extends Card{
    public GuardCard(String name_of_card, int card_value) {

        this.name_of_card = name_of_card;
        this.card_value = card_value;
    }

    @Override
    String getCardName() {
        return name_of_card;
    }

    @Override
    int getCardValue() {
        return card_value;
    }

    @Override
    void handlecard(Player playerPlayingCard) {
        Player targetPlayer;
        String playername;
        for (Player player : players) {
            if (player.inGame &&                        // other player must still be in the game
                    !player.isGuarded &&                    // and must not be guarded
                    (this.name_of_card == "PRINCE" || player != playerPlayingCard)  )   // and must not choose himself, unless for the prince (discarding own card is allowed)
            {
                availablePlayers.add(player);
            }
        }

        // TODO Display the player name from the availablePlayers so that the player can choose the name

        // TODO Read the input of the user
        // The input from the player is String, but the targetPlayer is of Playertype


        // TODO Set the targetPlayer as per users choice



        if (availablePlayers.contains(playername)) {

            targetPlayer = players.equals(playername);
            String guess_cardname;
            // Then currentplayer sees the guess the card of the targetPlayer

            // hier we could print out the message to the current player to choose the card
            System.out.println("What card do you think the target player has?");
            guess_cardname = read input from the client;

            if (guess_cardname == this.name_of_card ) {
                System.out.println("You cannot choose the guard name");
            }else if(guess_cardname.equals(targetPlayer.getCard1().getCardName())) {
                System.out.println("your guess was correct");

                //If the guess ic correct we set the player setInGame to false and he will be out of the round.
                targetPlayer.setInGame(false);
            }else {
                System.out.println("your guess was incorrect.");
            }
        }else {
            System.out.println("Choos the name from the list of available players")
        }

    }

    }
}
