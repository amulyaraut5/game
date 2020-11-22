package card;

import game.Player;

public class CountessCard extends Card {
    public CountessCard(String nameOfCard, int cardValue){
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
     * Notifies all players that the countess has been discarded.
     * The Countess takes effect while she is in the hand, which is already handled in Round class.
     * @param playerPlayingCard the current player
     */
    @Override
    public void handleCard(Player playerPlayingCard) {
        for (Player player : round.getActivePlayers()) {
            if(player != playerPlayingCard) {
                availablePlayers.add(player);
            }
        }

        for (Player player : availablePlayers){
            player.message(playerPlayingCard + " has discarded the Countess");
        }
    }
}
