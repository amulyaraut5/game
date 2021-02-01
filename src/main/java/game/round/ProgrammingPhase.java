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
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.*;
import utilities.enums.CardType;

import java.util.ArrayList;
import java.util.Random;

/**
 * In this class the players put down their programming cards for the five registers.
 *
 * @author janau, sarah
 */

public class ProgrammingPhase extends Phase {

    private static final Logger logger = LogManager.getLogger();
    /**
     * saves the player ID's. A player gets removed if he has already chosen 5 cards before the timer runs out
     */
    private final ArrayList<Player> notReadyPlayers = new ArrayList<>();
    private boolean timerFinished = false;

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

        for (Player player : players) {
            notReadyPlayers.add(player);

            //discard all Programming cards left in the registers and put all played Damage Cards back to their decks. Then create an empty register
            if (!(player.getRegisterCards().contains(null))) {
                player.discardCards(player.getRegisterCards(), player.getDiscardedProgrammingDeck());
                player.createRegister();
            }

            //draw 9 cards to program the robot
            drawProgrammingCards(9, player);
            ArrayList<CardType> cards = new ArrayList<>(9);
            for (Card card : player.getDrawnProgrammingCards()) {
                cards.add(card.getName());
            }
            player.message(new YourCards(cards, player.getID()));
            server.communicateUsers((new NotYourCards(player.getID(), player.getDrawnProgrammingCards().size())), player);
        }
    }

    /**
     * If a player puts a card (or removes one) into a register it is saved there.
     * If the player has filled all 5 registers and he is the first one to do so the timer is started.
     *
     * @param player     The player moved a card in the register
     * @param selectCard Message that was received
     */
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
                //case Null -> chosenCard = null;
            }
        }

        if (chosenCard != null) {
            //put the card in the register
            ArrayList<CardType> cardTypes = new ArrayList<>();
            for (Card card : player.getDrawnProgrammingCards()) {
                cardTypes.add(card.getName());
            }
            if (cardTypes.contains(type)) {
                player.setRegisterCards(selectCard.getRegister(), chosenCard);
            } else {
                server.communicateDirect(new Error("This is not a valid card."), player.getID());
            }

            //logger.info("drawn2" + player.getDrawnProgrammingCards());
            //logger.info("register2" + player.getRegisterCards());

            //Here the card with the same cardType enum as the chosen card is removed from the hand cards
            CardType chosenCardType = chosenCard.getName();
            for (Card card : player.getDrawnProgrammingCards()) {
                cardTypes.add(card.getName());
            }

            for (int i = 0; i < cardTypes.size(); i++) {
                if (cardTypes.get(i) == chosenCardType) {
                    player.getDrawnProgrammingCards().remove(i);
                    break;
                }
            }
            //if a card was removed, remove the card from the register and put it in the hand cards
        } else {
            Card removeCard = player.getRegisterCard(selectCard.getRegister());
            player.getDrawnProgrammingCards().add(removeCard);
            player.setRegisterCards(selectCard.getRegister(), null);
        }

        //logger.info("drawn3" + player.getDrawnProgrammingCards());
        //logger.info("register3" + player.getRegisterCards());
        //logger.info("discard3" + player.getDiscardedProgrammingDeck().getDeck());

        //if this player put a card in each register he is removed from the notReadyPlayer List and discards the rest of his programming hand cards
        if (!player.getRegisterCards().contains(null)) {
            notReadyPlayers.remove(player);
            server.communicateAll(new SelectionFinished(player.getID()));
            player.discardCards(player.getDrawnProgrammingCards(), player.getDiscardedProgrammingDeck());

            //logger.info("discard4" + player.getDiscardedProgrammingDeck().getDeck());
            //logger.info("draw4" + player.getDrawProgrammingDeck().getDeck());

            //If this player is the first to finish the timer starts
            if (notReadyPlayers.size() == players.size() - 1) {
                startProgrammingTimer();
                //If all players are ready the timer ends early
            } else if (notReadyPlayers.isEmpty()) {
                endProgrammingTimer();
            }
        }
        //TODO if a player doesn't play this round use isFinished?
    }

    /**
     * Starts a 30 second timer when the first player filled all 5 registers.
     */
    private void startProgrammingTimer() {
        GameTimer gameTimer = new GameTimer(this);
        gameTimer.setName("GameTimer");
        gameTimer.start();
    }

    /**
     * Ends the timer. Either if 30sec ran through or if all players finished their selection.
     * If some players still haven't filled their registers the dealRandomCard() method is called.
     */
    public void endProgrammingTimer() {
        /*for (Player player : playerList) {
            logger.info("discard5" + player.getDiscardedProgrammingDeck().getDeck());
            logger.info("draw5" + player.getDrawProgrammingDeck().getDeck());
        }*/
        if (!(timerFinished)) {
            timerFinished = true;
            ArrayList<Integer> playerIDs = new ArrayList<>();
            for (Player player : notReadyPlayers) {
                playerIDs.add(player.getID());
            }
            server.communicateAll(new TimerEnded(playerIDs));
            if (!(notReadyPlayers.isEmpty())) {
                dealRandomCards();
            }
            game.nextPhase();
        }
    }

    /**
     * For every player that didn't manage to put down their cards in time all cards on their hand and registers get discarded.
     * They draw 5 programming cards and put them on their registers in random order
     */
    private void dealRandomCards() {

       /*for (Player player : playerList) {
            logger.info("discard6" + player.getDiscardedProgrammingDeck().getDeck());
            logger.info("draw6" + player.getDrawProgrammingDeck().getDeck());
        }*/

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
            player.message(new DiscardHand(player.getID()));

            //Take 5 cards from the draw Deck and put them down in random order
            drawProgrammingCards(5, player);
            fillRegisters(player);

            //save the CardNames of cards to send them in CardsYouGotNow
            ArrayList<CardType> cardNames = new ArrayList<>();
            for (Card card : player.getRegisterCards()) {
                cardNames.add(card.getName());
            }
            player.message(new CardsYouGotNow(cardNames));
        }

        /*for (Player player : playerList) {
            logger.info("discard7" + player.getDiscardedProgrammingDeck().getDeck());
            logger.info("draw7" + player.getDrawProgrammingDeck().getDeck());
        }*/
    }

    /**
     * This methods fills the players register with 5 cards in random order
     * Also it makes sure that there is no Again card in the first register
     *
     * @param player who needs to fill his registers
     */

    private void fillRegisters(Player player) {
        Random random = new Random();
        for (int register = 1; register < 6; register++) {
            ArrayList<Card> availableCards = player.getDrawnProgrammingCards();

            //choose a card depending on a randomly generated index
            Card randomElement = availableCards.get(random.nextInt(availableCards.size()));
            if (register == 1 && randomElement.getName() == CardType.Again) {
                fillRegisters(player);
            }
            player.setRegisterCards(register, randomElement);
            player.getDrawnProgrammingCards().remove(randomElement);
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
        int currentDeckSize = player.getDrawProgrammingDeck().getDeck().size();
        if (!(currentDeckSize < amount)) {
            player.setDrawnProgrammingCards(player.getDrawProgrammingDeck().drawCards(amount));
        } else {
            player.setDrawnProgrammingCards(player.getDrawProgrammingDeck().drawCards(currentDeckSize));
            player.reuseDiscardedDeck();
            server.communicateAll(new ShuffleCoding(player.getID()));
            player.getDrawnProgrammingCards().addAll(player.getDrawProgrammingDeck().drawCards(amount - currentDeckSize));
        }
    }

    public ArrayList<Player> getNotReadyPlayers() {
        return notReadyPlayers;
    }
}