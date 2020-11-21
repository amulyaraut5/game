package card;

import game.Player;

public class GuardCard extends Card{
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
     * By calling this method player  designates another player and names a type of card. If that players has that card
     * then the player will be out of the round.
     * However a player cannot name GUARD card.
     * @param playerPlayingCard
     */
    @Override
    public void handlecard(Player playerPlayingCard) {

        String targetplayername = null;
        String guess_cardname = null;

        for (Player player : players) {
            if (player.inGame &&                        // other player must still be in the game
                    !player.isGuarded)                  // and must not be guarded
            {
                availablePlayers.add(player);
            }
        }

        // TODO Display the player name from the availablePlayers so that the player can choose the name
        // TODO Change the println statement
        // Print the name from the Set<Player>....
        for(Player player : availablePlayers) {
            System.out.println(player.getName());
        }
        // TODO Read the input of the user
        // The input from the player is String, but the targetPlayer is of Player type!!!!!!!!

        // TODO Set the targetPlayer as per users choice

        //THOUGHT

        for(Player targetPlayer: availablePlayers){

            if (targetPlayer.getName().equals(targetplayername)){
                // Then playerPlayingCard  can guess the card of the targetPlayer

                //TODO change every println statement!!!!!!!!!!!!!!!
                System.out.println("What card do you think the target player has?");

                //TODO read the input from the Player
                //guess_cardname = ;

                if (guess_cardname == this.name_of_card ) {
                    System.out.println("You cannot choose the guard name");

                }else if(guess_cardname.equals(targetPlayer.getCard().getCardName())) {
                    System.out.println("Your guess was correct");

                    //If the guess ic correct we set the player setInGame to false and he will be out of the round.
                    targetPlayer.setInGame(false);
                }else {
                    System.out.println("Your guess was Incorrect.");
                }

            }
        }

    }
}
