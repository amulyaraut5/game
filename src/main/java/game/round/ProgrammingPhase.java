package game.round;

import game.Player;
import game.gameObjects.cards.Card;
import game.gameObjects.decks.ProgrammingDeck;
import server.Server;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.NotYourCards;
import utilities.JSONProtocol.body.SelectCard;
import utilities.JSONProtocol.body.YourCards;

import java.util.ArrayList;

public class ProgrammingPhase  {

    private ArrayList<Player> playerList;
    /**
     * timerIsRunning will get true if a player creates an instance of timer
     */
    private boolean timerIsRunning = false;

    /**
     * a player gets removed if he has already chose 5 cards in the time
     */
    private ArrayList<Player> notReadyPlayers = new ArrayList<>();

    /**
     * Programming cards from which the player can choose to program his robot.
     */
    private ArrayList<Card> availableProgrammingCards;

    public ProgrammingPhase(Round round) {
        this.playerList = round.getPlayerList();
    }
    public void startProgrammingPhase() {
        dealProgrammingCards();
        //send Protocol to player and others which cards the player has
        for(Player player: playerList){
            ArrayList<Card> cardDeck = player.getDrawProgrammingDeck().getDeck();
            JSONMessage toPlayer = new JSONMessage(new YourCards(cardDeck));
            JSONMessage toOtherPlayers = new JSONMessage(new NotYourCards(player.getId(), cardDeck.size()));
            Server.getInstance().communicateUsers(toOtherPlayers, player.getThread());
            player.message(toPlayer);
        }

    }

    /**
     * if
     * @param player
     * @param selectCard
     */
    //TODO server has to call this method if he gets the protocol cardselected
    private void cardWasSelected(Player player, SelectCard selectCard){
        player.setRegisterAndCards(selectCard.getRegister(), selectCard.getCard());
    }
    /**
     * every player can look at their programming cards
     */
    private void showCards() {

    }

    /**
     * players get their  cards for programming their robot in this round.
     * If the draw pile has at least 9 cards, the top 9 cards get dealt.
     * If there are less cards, the remaining cards get dealt.
     * the discarded pile is reshuffled and used as the draw pile and the rest of the 9 cards is drawn from
     * the new draw Deck.
     */
    private void dealProgrammingCards() {
        for (Player player : playerList) {
            ProgrammingDeck currentDeck = player.getDrawProgrammingDeck();
            if (currentDeck.size() >= 9) {
                availableProgrammingCards = currentDeck.drawCards(9);
            } else {
                //TODO send ShuffleCoding
                availableProgrammingCards = currentDeck.drawCards(currentDeck.size());
                player.reuseDiscardedDeck();
                availableProgrammingCards.addAll(player.getDrawProgrammingDeck().drawCards(9- currentDeck.size()));
                }
            player.setDrawnProgrammingCards(availableProgrammingCards);
            }

        }



    /**
     * a method which handles the players who didn´t choose cards in time
     */
    private void timeRunOut() {
        // random set the programming cards of players´ deck to the registerAndCard Map
    }

    /**
     * this method gets called after every Round to reset the attributes
     */
    private void resetProgrammingPhase() {
        timerIsRunning = false;
        notReadyPlayers = null;
    }

}