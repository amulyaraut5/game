package game.round;

import game.Player;
import game.gameObjects.cards.Card;
import game.gameObjects.cards.programming.Again;
import game.gameObjects.cards.programming.MoveI;
import game.gameObjects.cards.programming.MoveII;
import game.gameObjects.cards.programming.MoveIII;
import game.gameObjects.decks.ProgrammingDeck;
import utilities.JSONProtocol.body.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * The Programming phase is the second Phase of every round.
 * In this class the players put down their programming cards for the five registers.
 *
 * @author janau, sarah
 */

public class ProgrammingPhase extends Phase {

    /**
     * saves the player id's. a player gets removed if he has already chose 5 cards in the time
     */
    private ArrayList<Integer> notReadyPlayers = new ArrayList<>();
    private boolean timerFinished = false;
    private ArrayList<Card> discardCards = new ArrayList<>();

    public ProgrammingPhase() {
        super();
        //discard all Programming cards left in the registers and create empty register
        for (Player player : playerList) {
            notReadyPlayers.add(player.getId());
            player.discardCards(player.getRegisterCards(), player.getDiscardedProgrammingDeck());
            player.createRegister();
        }
        dealProgrammingCards();
    }

    @Override
    public void startPhase() {

    }


    /**
     * if
     *
     * @param player
     * @param selectCard
     */
    //Damit müsste die Reihenfolge wie folgt sein: "TimerEnded" (an alle) ->
    // "DiscardHand" (an den Spieler der seine Hand wegwerfen muss)  -> CardsYouGotNow (neugezogene Karten).
    // Spielt denn die Reihenfolge von "TimerEnded" und "DiscardHand" eine für euch relevante Rolle
    // (weil soweit ich das sehe ist das ja im gleichen Zeitschritt)
    //TODO server has to call this method if he gets the protocol cardselected
    private void putCardToRegister(Player player, SelectCard selectCard) {
        String cardType = selectCard.getCard();
        Card chosenCard = null;
        switch (cardType) {
            case "again":
                chosenCard = new Again();
                break;
            case "moveI":
                chosenCard = new MoveI();
                break;
            case "moveII":
                chosenCard = new MoveII();
                break;
            case "moveIII":
                chosenCard = new MoveIII();
                break;
            //TODO other types of cards
            case "null":
                // if player removes one card
                break;
        }

        //put the card in the register and remove it from the players hand
        player.setRegisterCards(selectCard.getRegister(), chosenCard);
        player.getDrawnProgrammingCards().remove(chosenCard);

        //if this player put a card in each register he is removed from the notReadyPlayer List and discards the rest of his programming hand cards
        if (!player.getRegisterCards().contains(null)) {
            notReadyPlayers.remove(player.getId());
            player.discardCards(player.getDrawnProgrammingCards(), player.getDiscardedProgrammingDeck());
            //If this player is the first to finish the timer starts
            if (notReadyPlayers.size() == playerList.size() - 1) {
                startProgrammingTimer(player);
                //If all players are ready the timer ends early
            } else if (notReadyPlayers.isEmpty()) {
                endProgrammingTimer();
            }
        }
        //TODO if a player doesn't play this round use isFinished?
    }


    /**
     * TODO
     *
     * @param player
     */
    private void startProgrammingTimer(Player player) {
        //isFinished = true;
        server.communicateAll(new SelectionFinished(player.getId()));
        GameTimer gameTimer = new GameTimer(this);
        gameTimer.start();
    }

    /**
     * method that gets called from gameTimer if he has ended and then sends the message
     * TimerEnded and calls dealRandomCards() if some players haven't filled their registers yet
     */
    public void endProgrammingTimer() {
        if (timerFinished) {
            timerFinished = true;
            server.communicateAll(new TimerEnded(notReadyPlayers));
            if (!(notReadyPlayers.isEmpty())) {
                dealRandomCards();
            }
            game.nextPhase();
        }
    }

    /**
     * For every player that didn't manage to put down their cards in time all cardss on their hand and registers get discarded.
     * They draw 5 programming cards and put them on their registers in random order
     */
    private void dealRandomCards() {

        //this method is only handled for players who didn't manage to put their cards down in time
        for (Integer id : notReadyPlayers) {
            Player player = game.getPlayerFromID(id);

            //Take all cards from the register and discard them to have an empty register
            player.discardCards(player.getRegisterCards(), player.getDiscardedProgrammingDeck());
            player.createRegister();

            //Discard all hand cards
            player.discardCards(player.getDrawnProgrammingCards(), player.getDiscardedProgrammingDeck());
            // TODO: still needed? player.getDrawnProgrammingCards().clear();
            player.message(new DiscardHand(player.getId()));

            //Take 5 cards from the draw Deck
            drawProgrammingCards(5, player);

            //Put the 5 drawn cards down in random order
            Random random = new Random();
            for (int register = 1; register < 6; register++) {
                ArrayList<Card> availableCards = player.getDrawnProgrammingCards();
                //choose a card depending on a randomly generated index
                Card randomElement = availableCards.get(random.nextInt(availableCards.size()));
                player.setRegisterCards(register, randomElement);
                player.getDrawnProgrammingCards().remove(randomElement);
            }
            player.message(new CardsYouGotNow(player.getRegisterCards()));
        }
    }

    /**
     * players get their cards for programming their robot in this method.
     * YourCards and NotYourCards protocol is send.
     */
    private void dealProgrammingCards() {
        for (Player player : playerList) {
            drawProgrammingCards(9, player);
            player.message(new YourCards(player.getDrawnProgrammingCards()));
            server.communicateUsers((new NotYourCards(player.getId(), player.getDrawnProgrammingCards().size())), player);
        }
    }

    /**
     * this method draws a given number of cards from the drawPile and adds it to the discarded Pile.
     * if the draw pile doesn't provide enough cards the discarded Pile is shuffled and used to draw remaining cards.
     *
     * @param amount of cards to draw
     * @param player player that needs to draw cards
     */
    private void drawProgrammingCards (int amount, Player player) {
        ProgrammingDeck currentDeck = player.getDrawProgrammingDeck();
        if (!(currentDeck.size() < amount)) {
            player.setDrawnProgrammingCards(player.getDrawProgrammingDeck().drawCards(amount));
        } else {
            player.setDrawnProgrammingCards(player.getDrawProgrammingDeck().drawCards(currentDeck.size()));
            player.reuseDiscardedDeck();
            player.getDrawnProgrammingCards().addAll(player.getDrawProgrammingDeck().drawCards(amount - currentDeck.size()));
            player.message(new ShuffleCoding(player.getId()));
        }
    }
}