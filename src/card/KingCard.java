package card;

import game.Player;

public class KingCard extends Card {
    public KingCard(String name_of_card, int card_value){
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


    /**
     * By calling this method the playing trades card with another card
     * @param playerPlayingCard
     */
    @Override
    void handlecard(Player playerPlayingCard) {
        for (Player player : players) {
            if (player.inGame &&                        // other player must still be in the game
                    !player.isGuarded)                // and must not be guarded)
            {
                availablePlayers.add(player);
            }
        }
        // TODO Display the player name from the availablePlayers so that the player can choose the name


        // TODO Read the input of the user

        // TODO Set the targetPlayer as per users choice from the list of players

        for(Player targetPlayer: availablePlayers){
            if(playerPlayingCard.getCard().getCardName().equals("king")){
                Card temp = targetPlayer.getCard();
                targetPlayer.setCard(playerPlayingCard.getCard());
                playerPlayingCard.setCard(temp);

            }

        }
    }


}
