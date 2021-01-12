package game.round;

import game.Player;
import game.gameObjects.cards.Card;
import game.gameObjects.cards.programming.Again;
import game.gameObjects.cards.programming.MoveI;
import game.gameObjects.cards.programming.MoveII;
import game.gameObjects.cards.programming.MoveIII;
import game.gameObjects.decks.ProgrammingDeck;
import utilities.JSONProtocol.body.*;
import utilities.Utilities;

import java.util.ArrayList;
import java.util.Collections;

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

    /**
     * Programming cards from which the player can choose to program his robot.
     */
    private ArrayList<Card> availableProgrammingCards;


    public ProgrammingPhase() {
        super();
        for (Player player : playerList) {
            notReadyPlayers.add(player.getId());
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
            player.discardCards(availableProgrammingCards, player.getDiscardedProgrammingDeck());
            if (notReadyPlayers.size() == playerList.size() - 1)
                onePlayerFinished(player);
        } //TODO if a player doesn't play this round use isFinished?
    }


    private void onePlayerFinished(Player player) {
        //isFinished = true;
        server.communicateAll(new SelectionFinished(player.getId()));
        GameTimer gameTimer = new GameTimer(this);
        gameTimer.start();

    }

    /**
     * method that gets called from gameTimer if he has ended and then sends the message
     * TimerEnded and calls dealRandomCards()
     */
    public void timerHasEnded() {
        server.communicateAll(new TimerEnded(notReadyPlayers));
        if (!(notReadyPlayers.size() == 0)) {
            dealRandomCards();
        }
        round.nextPhase(Utilities.Phase.PROGRAMMING);
    }

    /**
     * Method takes all previously dealt programming cards (9), shuffles them and puts the top 5
     * cards into the players register
     */
    private void dealRandomCards() {
        for (Integer id : notReadyPlayers) {
            Player player = game.getPlayerFromID(id);

            player.message(new DiscardHand(player.getId()));

            ArrayList<Card> registers = player.getRegisterCards();
            availableProgrammingCards = player.getDrawnProgrammingCards();

            registers.removeAll(null);
            availableProgrammingCards.addAll(registers);
            Collections.shuffle(availableProgrammingCards);

            for (int i = 0; i < registers.size(); i++) {
                player.setRegisterCards(i, availableProgrammingCards.get(i));
                availableProgrammingCards.set(i, null);
            }

            availableProgrammingCards.removeAll(null);
            player.discardCards(availableProgrammingCards, player.getDiscardedProgrammingDeck());
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
                availableProgrammingCards = currentDeck.drawCards(9);
            } else {
                availableProgrammingCards = currentDeck.drawCards(currentDeck.size());
                player.reuseDiscardedDeck();
                availableProgrammingCards.addAll(player.getDrawProgrammingDeck().drawCards(9 - currentDeck.size()));

                player.message(new ShuffleCoding(player.getId()));
            }
            player.setDrawnProgrammingCards(availableProgrammingCards);
            player.message(new YourCards(availableProgrammingCards));
            server.communicateUsers((new NotYourCards(player.getId(), availableProgrammingCards.size())), player);
        }

    }


}