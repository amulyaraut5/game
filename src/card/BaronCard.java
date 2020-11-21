package card;

import game.Player;

public class BaronCard extends Card {
    public BaronCard(String name_of_card, int card_value){
        this.name_of_card = name_of_card;
        this.card_value = card_value;
    }

    @Override
    String getCardName() {
        return this.name_of_card;
    }

    @Override
    public int getCardValue() {
        return card_value;
    }


    /**
     * By calling this method player will choose the targetPlayer and privately compare the cards.
     * The Player with the lower card_value will be eliminated from the round.
     * @param playerPlayingCard
     */
    @Override
    void handlecard(Player playerPlayingCard) {

        String targetplayername = null;

        for (Player player : players) {
            if (player.inGame &&                        // other player must still be in the game
                    !player.isGuarded)                // and must not be guarded)   // and must not choose himself, unless for the prince (discarding own card is allowed)
            {
                availablePlayers.add(player);
            }
        }
        // TODO Display the player name from the availablePlayers so that the player can choose the name


        // TODO Read the input of the user

        // TODO Set the targetPlayer as per users choice from the list of players

        for(Player targetPlayer: availablePlayers){

            if (targetPlayer.getName().equals(targetplayername)){
                // Then playerPlayingCard   see the card of the targetPlayer
                // and compares the hand to see who has the greater card_value


                // Get the card of targetPlayer and show only to the player playing card
                int targetPlayerCard = targetPlayer.getCard().getCardValue();
                int playerPlaying = playerPlayingCard.getCard().getCardValue();

                if(targetPlayerCard > playerPlaying){
                    targetPlayer.setInGame(false);
                } else {
                    playerPlayingCard.setInGame(false);
                }
            }

        }

    }


}
