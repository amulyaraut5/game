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
import java.util.Timer;

/**
 * The Programming phase is the second Phase of every round.
 * In this class the players choose their programming cards for the five registers.
 *
 * @author janau, sarah
 */

public class ProgrammingPhase extends Phase {
    /**
     * timerIsRunning will get true if a player creates an instance of timer
     */
    private boolean timerIsRunning = false;

    /**
     * saves the player id's. a player gets removed if he has already chose 5 cards in the time
     */
    private ArrayList<Integer> notReadyPlayers = new ArrayList<>();

    /**
     * Programming cards from which the player can choose to program his robot.
     */
    private ArrayList<Card> availableProgrammingCards;

    public ProgrammingPhase() {
    }

    @Override
    public void startPhase() {
        for (Player player : playerList) {
            notReadyPlayers.add(player.getId());
        }
        dealProgrammingCards();
        while (!(notReadyPlayers.size() == 0 )) {
            //die Programming Phase geht solange, bis alle Spieler ihre Register gefüllt haben
        }
        resetProgrammingPhase();
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
    private void cardWasSelected(Player player, SelectCard selectCard) {
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
        if (player.getRegisterCards().size() == 5 && !player.getRegisterCards().contains(null)) {
            notReadyPlayers.remove(player.getId());
            if (notReadyPlayers.size() == playerList.size() - 1) {
                onePlayerFinished(player);
            }
        }
    }


    /**
     * every player can look at their programming cards -> Turn cards around after everyone's cards were dealt?
     */
    private void showCards() {
    }

    public void onePlayerFinished(Player player) {
        server.communicateAll(new SelectionFinished(player.getId()));
        server.communicateAll(new TimerStarted());
        Timer timer = new Timer(true); //TODO einzelne Klasse überhaupt notwendig oder sogar wait()?
        //timer for 30 sek
        server.communicateAll(new TimerEnded(notReadyPlayers));
        if (!(notReadyPlayers.size() == 0)) {
            timeRanOut();
        }
        for (Player playerTest : playerList) {
            if (playerTest.getRegisterCards().size() < 5) {
                playerTest.message(new DiscardHand(playerTest.getId()));
                timeRanOut();
            }
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
                availableProgrammingCards = currentDeck.drawCards(9);
            } else {
                availableProgrammingCards = currentDeck.drawCards(currentDeck.size());
                player.reuseDiscardedDeck();
                availableProgrammingCards.addAll(player.getDrawProgrammingDeck().drawCards(9 - currentDeck.size()));

                player.message(new ShuffleCoding(player.getId()));
            }
            player.setDrawnProgrammingCards(availableProgrammingCards);
            player.message(new YourCards(availableProgrammingCards));
            server.communicateUsers((new NotYourCards(player.getId(), availableProgrammingCards.size())), player.getThread());
        }

    }


    /**
     * Method that chooses ar random card out of the drawn Programming cards for every empty register.
     * TODO call method and send CardsYouGotNow protocol
     */
    private void timeRanOut() {
        //TODO get player from ID
        for (Player player : playerList) {
            ArrayList<Card> registers = player.getRegisterCards();
            if (!(registers.size() == 5) || registers.contains(null)) {
                Random randomGenerator = new Random();
                for (int i = 0; i < registers.size(); i++) {
                    if (registers.get(i) == null) {
                        availableProgrammingCards = player.getDrawnProgrammingCards();
                        int index = randomGenerator.nextInt(availableProgrammingCards.size());
                        Card randomCard = availableProgrammingCards.get(index);
                        availableProgrammingCards.remove(index);
                        player.setRegisterCards(i + 1, randomCard);
                    }
                }
            }
            player.message(new CardsYouGotNow(player.getRegisterCards()));
        }
    }

    /**
     * this method gets called after every Round to reset the attributes
     * TODO is it necessary? A new Phase gets created the next time anyway
     */
    private void resetProgrammingPhase() {
        timerIsRunning = false;
        notReadyPlayers = null;
    }
}