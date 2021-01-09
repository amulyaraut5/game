package game.round;

import game.Player;
import game.gameObjects.cards.Card;
import game.gameObjects.cards.programming.Again;
import game.gameObjects.cards.programming.MoveI;
import game.gameObjects.cards.programming.MoveII;
import game.gameObjects.cards.programming.MoveIII;
import game.gameObjects.decks.ProgrammingDeck;
import server.Server;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Timer;

public class ProgrammingPhase {

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

    /**
     * saves if one player already filled his 5 registers
     */
    Boolean onePlayerFinished = false;

    public ProgrammingPhase(Round round) {
        this.playerList = round.getPlayerList();
    }

    public void startProgrammingPhase() {
        dealProgrammingCards();
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
                chosenCard = null; // player can empty a register
                break;
        }

        player.setRegisterAndCards(selectCard.getRegister(), chosenCard);
        if (!onePlayerFinished) {
            if (player.getRegisterAndCards().size() == 5 && !player.getRegisterAndCards().containsValue(null)) {
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
        onePlayerFinished = true;
        JSONMessage selectionFinished = new JSONMessage(new SelectionFinished(player.getId()));
        Server.getInstance().communicateAll(selectionFinished);
        JSONMessage timerStarted = new JSONMessage(new TimerStarted());
        // JSONMessage timerEnded = new JSONMessage(new TimerEnded(//TODO));
        Server.getInstance().communicateAll(timerStarted);
        Timer timer = new Timer(true); //TODO einzelne Klasse überhaupt notwendig oder sogar wait()?
        //timer for 30 sek
        JSONMessage discardCard;
        JSONMessage cardsYouGotNow;
        for (Player playerTest : playerList) {
            if (playerTest.getRegisterAndCards().size() < 5) {
                discardCard = new JSONMessage(new DiscardHand(playerTest.getId()));
                playerTest.message(discardCard);
                //TODO random 5 cards from programmingCardDeck
                //cardsYouGotNow = new JSONMessage(new CardsYouGotNow());
                //playerTest.message(cardsYouGotNow);
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

                JSONMessage shuffleCoding = new JSONMessage(new ShuffleCoding(player.getId()));
                player.message(shuffleCoding);
            }
            player.setDrawnProgrammingCards(availableProgrammingCards);
            JSONMessage yourCards = new JSONMessage(new YourCards(availableProgrammingCards));
            player.message(yourCards);
            JSONMessage notYourCards = new JSONMessage(new NotYourCards(player.getId(), availableProgrammingCards.size()));
            Server.getInstance().communicateUsers(notYourCards, player.getThread());
        }

    }


    /**
     * Method that chooses ar random card out of the drawn Programming cards for every empty register.
     * TODO call method and send CardsYouGotNow protocol
     */
    private void timeRanOut() {
        for (Player player : playerList) {
            if (!(player.getRegisterAndCards().size() == 5) || player.getRegisterAndCards().containsValue(null)) {
                Map<Integer, Card> register = player.getRegisterAndCards();
                Random randomGenerator = new Random();
                for (Map.Entry<Integer, Card> registerAndCard : register.entrySet()) {
                    if (registerAndCard.getValue() == null) {
                        availableProgrammingCards = player.getDrawnProgrammingCards();
                        int index = randomGenerator.nextInt(availableProgrammingCards.size());
                        Card randomCard = availableProgrammingCards.get(index);
                        availableProgrammingCards.remove(index);
                        player.setRegisterAndCards(registerAndCard.getKey(), randomCard);
                    }
                }
            }
        }
    }

    /**
     * this method gets called after every Round to reset the attributes
     */
    private void resetProgrammingPhase() {
        timerIsRunning = false;
        notReadyPlayers = null;
    }

}