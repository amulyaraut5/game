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
 * In this class the players choose their programming cards for the five registers.
 *
 * @author janau, sarah
 */

public class ProgrammingPhase extends Phase {

    /**
     * saves the player id's. a player gets removed if he has already chose 5 cards in the time
     */
    private ArrayList<Integer> notReadyPlayers = new ArrayList<>();
    private boolean timerFinished = false;

    public ProgrammingPhase() {
        super();
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

        player.setRegisterCards(selectCard.getRegister(), chosenCard);
        player.getDrawnProgrammingCards().remove(chosenCard);
        if (player.getRegisterCards().size() == 5 && !player.getRegisterCards().contains(null)) {
            notReadyPlayers.remove(player.getId());
            player.discardCards(player.getDrawnProgrammingCards(), player.getDiscardedProgrammingDeck());
            if (notReadyPlayers.size() == playerList.size() - 1) {
                startProgrammingTimer(player);
            } else if (notReadyPlayers.isEmpty()) {
                endProgrammingTimer();
            }
        }
        //TODO if a player doesn't play this round use isFinished?
    }


    private void startProgrammingTimer(Player player) {
        //isFinished = true;
        server.communicateAll(new SelectionFinished(player.getId()));
        GameTimer gameTimer = new GameTimer(this);
        gameTimer.start();

    }

    /**
     * method that gets called from gameTimer if he has ended and then sends the message
     * TimerEnded and calls dealRandomCards()
     */
    public void endProgrammingTimer() {
        if(timerFinished){
            timerFinished = true;
            server.communicateAll(new TimerEnded(notReadyPlayers));
            if (!(notReadyPlayers.size() == 0)) {
                dealRandomCards();
            }
            game.nextPhase();

        }

    }

    /**
     * Method takes all previously dealt programming cards (9), shuffles them and puts the top 5
     * cards into the players register
     */
    private void dealRandomCards() {
        for (Integer id : notReadyPlayers) {
            Player player = game.getPlayerFromID(id);

            //Take all cards from the register and discard them to have an empty register
            player.getRegisterCards().removeAll(null);
            player.discardCards(player.getRegisterCards(), player.getDiscardedProgrammingDeck());
            player.createRegister();

            //Discard all hand cards
            player.discardCards(player.getDrawnProgrammingCards(), player.getDiscardedProgrammingDeck());
            player.getDrawnProgrammingCards().clear();
            player.message(new DiscardHand(player.getId()));

            //Take 5 cards from the draw Deck, and shuffle the discard deck if necessary
            ProgrammingDeck currentDeck = player.getDrawProgrammingDeck();
            if (!(currentDeck.size() < 5)) {
                player.setDrawnProgrammingCards(player.getDrawProgrammingDeck().drawCards(5));
            } else {
                player.setDrawnProgrammingCards(player.getDrawProgrammingDeck().drawCards(currentDeck.size()));
                player.reuseDiscardedDeck();
                player.getDrawnProgrammingCards().addAll(player.getDrawProgrammingDeck().drawCards(5 - currentDeck.size()));
            }

            //Put the 5 drawn cards down in random order
            Random random = new Random();
            for (int register = 1; register < 6; register++) {
                ArrayList<Card> availableCards = player.getDrawnProgrammingCards();
                Card randomElement = availableCards.get(random.nextInt(availableCards.size()));
                player.setRegisterCards(register, randomElement);
                player.getDrawnProgrammingCards().remove(randomElement);
            }

            player.message(new CardsYouGotNow(player.getRegisterCards()));
        }

    }

    /**
     * players get their  cards for programming their robot in this round.
     * If the draw pile has at least 9 cards, the top 9 cards get dealt.
     * If there are less cards, the remaining cards get dealt.
     * the discarded pile is reshuffled and used as the draw pile and the rest of the 9 cards is drawn from
     * the new draw Deck.
     * YourCards and NotYourCards protocol is send
     */
    private void dealProgrammingCards() {
        for (Player player : playerList) {
            ProgrammingDeck currentDeck = player.getDrawProgrammingDeck();
            if (currentDeck.size() >= 9) {
                player.setDrawnProgrammingCards(player.getDrawProgrammingDeck().drawCards(9));
            } else {
                player.setDrawnProgrammingCards(player.getDrawProgrammingDeck().drawCards(currentDeck.size()));
                player.reuseDiscardedDeck();
                player.getDrawnProgrammingCards().addAll(player.getDrawProgrammingDeck().drawCards(9 - currentDeck.size()));
                player.message(new ShuffleCoding(player.getId()));
            }
            player.message(new YourCards(player.getDrawnProgrammingCards()));
            server.communicateUsers((new NotYourCards(player.getId(), player.getDrawnProgrammingCards().size())), player);
        }

    }


}