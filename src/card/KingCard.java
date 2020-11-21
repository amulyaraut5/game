package card;

import game.Player;

public class KingCard extends Card {
    public KingCard(String nameOfCard, int card_value){
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
        // TODO Change the println statement
        // Print the name from the Set<Player>....
        for(Player player : availablePlayers) {
            System.out.println(player.getName());
        }

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
