package game.round;

import game.Player;
import game.gameObjects.cards.Card;
import game.gameObjects.cards.damage.Spam;
import game.gameObjects.cards.damage.Trojan;
import game.gameObjects.cards.damage.Virus;
import game.gameObjects.cards.damage.Worm;
import game.gameObjects.cards.programming.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.body.*;
import utilities.enums.CardType;

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
    private ArrayList<Player> notReadyPlayers = new ArrayList<>();
    private boolean timerFinished = false;
    private ArrayList<Card> discardCards = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger();

    /**
     * starts the ProgrammingPhase.
     * All Players get added to notReadyPlayers to track who has/hasn't finished programming
     * If the register ist filled with cards from the previous round those get discarded.
     * After that new cards are dealt.
     */
    public ProgrammingPhase() {
        super();
        /*for (Player player : playerList) {
            logger.info("discard1" + player.getDiscardedProgrammingDeck().getDeck());
            logger.info("draw1" + player.getDrawProgrammingDeck().getDeck());
        }*/
        //discard all Programming cards left in the registers and create empty register
        for (Player player : playerList) {
            notReadyPlayers.add(player);
            if (!(player.getRegisterCards().contains(null))) {
                player.discardCards(player.getRegisterCards(), player.getDiscardedProgrammingDeck());
                player.createRegister();
                //player.message(new Movement(1,22));
            }
        }
        dealProgrammingCards();
        //<----- Test for Movement Protocol----->
        //server.communicateAll(new Movement(1, new Coordinate(4, 4).toPosition()));
        //server.communicateAll(new PlayerTurning(1, Orientation.LEFT));
        //server.communicateAll(new Movement(2, new Coordinate(1, 1).toPosition()));
        //server.communicateAll(new PlayerTurning(2, Orientation.RIGHT));
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
    public void putCardToRegister(Player player, SelectCard selectCard) {
        Card chosenCard = null;
        CardType type = selectCard.getCard();
        if (type != null) {
            switch (type) {
                case Again -> chosenCard = new Again();
                case MoveI -> chosenCard = new MoveI();
                case MoveII -> chosenCard = new MoveII();
                case MoveIII -> chosenCard = new MoveIII();
                case PowerUp -> chosenCard = new PowerUp();
                case BackUp -> chosenCard = new BackUp();
                case TurnLeft -> chosenCard = new TurnLeft();
                case TurnRight -> chosenCard = new TurnRight();
                case UTurn -> chosenCard = new UTurn();
                case Spam -> chosenCard = new Spam();
                case Trojan -> chosenCard = new Trojan();
                case Virus -> chosenCard = new Virus();
                case Worm -> chosenCard = new Worm();
            }
        }
        //put the card in the register and remove it from the players hand
        player.setRegisterCards(selectCard.getRegister(), chosenCard);
        //logger.info("drawn7" + player.getDrawnProgrammingCards());
        //logger.info("register8" + player.getRegisterCards());
        player.getDrawnProgrammingCards().remove(chosenCard);

        //logger.info("discard7" + player.getDrawnProgrammingCards());

        //if this player put a card in each register he is removed from the notReadyPlayer List and discards the rest of his programming hand cards
        if (!player.getRegisterCards().contains(null)) {
            notReadyPlayers.remove(player);
            player.discardCards(player.getDrawnProgrammingCards(), player.getDiscardedProgrammingDeck());
            //logger.info("discard4" + player.getDiscardedProgrammingDeck().getDeck());
            //logger.info("draw4" + player.getDrawProgrammingDeck().getDeck());
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
        server.communicateAll(new SelectionFinished(player.getID()));
        GameTimer gameTimer = new GameTimer(this);
        gameTimer.start();
    }

    /**
     * method that gets called from gameTimer if he has ended and then sends the message
     * TimerEnded and calls dealRandomCards() if some players haven't filled their registers yet
     */
    public void endProgrammingTimer() {
        /*for (Player player : playerList) {
            logger.info("discard5" + player.getDiscardedProgrammingDeck().getDeck());
            logger.info("draw5" + player.getDrawProgrammingDeck().getDeck());
        }*/
        if (!(timerFinished)) {
            timerFinished = true;
            ArrayList<Integer> playerIds = new ArrayList<>();
            for (Player player : notReadyPlayers) {
                playerIds.add(player.getID());
            }
            server.communicateAll(new TimerEnded(playerIds));
            if (!(notReadyPlayers.isEmpty())) {
                dealRandomCards();
            }
        }
        game.nextPhase();
    }

    /**
     * For every player that didn't manage to put down their cards in time all cards on their hand and registers get discarded.
     * They draw 5 programming cards and put them on their registers in random order
     */
    private void dealRandomCards() {

       /* for (Player player : playerList) {
            logger.info("discard2" + player.getDiscardedProgrammingDeck().getDeck());
            logger.info("draw2" + player.getDrawProgrammingDeck().getDeck());
        } */

        //this method is only handled for players who didn't manage to put their cards down in time
        for (Player player : notReadyPlayers) {

            //Take all cards from the register and discard them to have an empty register
            ArrayList<Card> tempCards = new ArrayList<>();
            for (Card card : player.getRegisterCards()) {
                if (card != null) {
                    tempCards.add(card);
                }
            }
            player.discardCards(tempCards, player.getDiscardedProgrammingDeck());
            player.createRegister();

            //Discard all hand cards
            player.discardCards(player.getDrawnProgrammingCards(), player.getDiscardedProgrammingDeck());
            // TODO: still needed? player.getDrawnProgrammingCards().clear();
            player.message(new DiscardHand(player.getID()));

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
            ArrayList<CardType> cardNames = new ArrayList<>();
            for (Card card : player.getRegisterCards()) {
                cardNames.add(card.getName());
            }

            player.message(new CardsYouGotNow(cardNames));
        }

        /*for (Player player : playerList) {
            logger.info("discard3" + player.getDiscardedProgrammingDeck().getDeck());
            logger.info("draw3" + player.getDrawProgrammingDeck().getDeck());
        }*/
    }

    /**
     * players get their cards for programming their robot in this method.
     * YourCards and NotYourCards protocol is send.
     */
    private void dealProgrammingCards() {
        for (Player player : playerList) {
            drawProgrammingCards(9, player);
            ArrayList<CardType> cards = new ArrayList<>(9);
            for (Card card : player.getDrawnProgrammingCards()) {
                cards.add(card.getName());
            }
            player.message(new YourCards(cards));
            server.communicateUsers((new NotYourCards(player.getID(), player.getDrawnProgrammingCards().size())), player);
        }
    }

    /**
     * this method draws a given number of cards from the drawPile and adds it to the discarded Pile.
     * if the draw pile doesn't provide enough cards the discarded Pile is shuffled and used to draw remaining cards.
     *
     * @param amount of cards to draw
     * @param player player that needs to draw cards
     */
    private void drawProgrammingCards(int amount, Player player) {
        ArrayList<Card> currentDeck = player.getDrawProgrammingDeck().getDeck();
        //System.out.println("1" + currentDeck.size());
        if (!(currentDeck.size() < amount)) {
            player.setDrawnProgrammingCards(player.getDrawProgrammingDeck().drawCards(amount));
        } else {
            player.setDrawnProgrammingCards(player.getDrawProgrammingDeck().drawCards(currentDeck.size()));
            player.reuseDiscardedDeck();
            player.getDrawnProgrammingCards().addAll(player.getDrawProgrammingDeck().drawCards(amount - currentDeck.size()));
            player.message(new ShuffleCoding(player.getID()));
        }
    }
}