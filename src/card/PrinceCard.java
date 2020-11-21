package card;

import game.Player;

public class PrinceCard extends Card {
    public PrinceCard(String name_of_card, int card_value) {
        this.name_of_card = name_of_card;
        this.card_value = card_value;
    }

    @Override
    String getCardName() {
        return name_of_card;
    }

    @Override
    public int getCardValue() {
        return card_value;
    }


    /**
     * By calling this method player can choose any player including themselves to discard their card
     * and draw a new card.If the discarded card is princess the player will be eliminated from the round.
     * @param playerPlayingCard
     */
    @Override
    void handlecard(Player playerPlayingCard) {

        for (Player player : players) {
            if (player.inGame &&                        // other player must still be in the game
                    !player.isGuarded &&                    // and must not be guarded
                    (this.name_of_card == "PRINCE" || player != playerPlayingCard))   //  can be prince (discarding own card is allowed)
            {
                availablePlayers.add(player);
            }
        }
        // TODO Display the player name from the availablePlayers so that the player can choose the name


        // TODO Read the input of the user

        // TODO Set the targetPlayer as per users choice from the list of players

        for (Player targetPlayer : availablePlayers) {
            // And targetPlayer can be playerPlayingCard which is already included in availablePlayers.
            // TODO Change the println statement

            if (targetPlayer.getCard().getCardName().equals("princess")) {
                // Display this messages to all the players.

                System.out.println("Target player has discarded a princess because of you! " +
                        "\nTarget player is now out of the game!" +
                        " \n*shakes fist angrily");
                targetPlayer.setInGame(false);
            }
        }
    }
}
