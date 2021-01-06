package game.round;

import game.Player;
import java.util.Map;
import java.util.Timer;

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
    //Damit müsste die Reihenfolge wie folgt sein: "TimerEnded" (an alle) ->
    // "DiscardHand" (an den Spieler der seine Hand wegwerfen muss)  -> CardsYouGotNow (neugezogene Karten).
    // Spielt denn die Reihenfolge von "TimerEnded" und "DiscardHand" eine für euch relevante Rolle
    // (weil soweit ich das sehe ist das ja im gleichen Zeitschritt)
    //TODO server has to call this method if he gets the protocol cardselected
    private void cardWasSelected(Player player, SelectCard selectCard){
        player.setRegisterAndCards(selectCard.getRegister(), selectCard.getCard());
        if (player.getRegisterAndCards().size() == 5) {
            onePlayerFinished(player);
        }
    }
    /**
     * every player can look at their programming cards
     */
    private void showCards() {

    public void onePlayerFinished(Player player){
        JSONMessage selectionFinished = new JSONMessage(new SelectionFinished(player.getId()));
        Server.getInstance().communicateAll(selectionFinished);
        JSONMessage timerStarted = new JSONMessage(new TimerStarted());
        // JSONMessage timerEnded = new JSONMessage(new TimerEnded(//TODO));
        Server.getInstance().communicateAll(timerStarted);
        Timer timer = new Timer(true); //TODO einzelne Klasse überhaupt notwendig oder sogar wait()?
        //timer for 30 sek
        JSONMessage discardCard;
        JSONMessage cardsYouGotNow;
        for (Player playerTest : playerList){
            if (playerTest.getRegisterAndCards().size()<5){
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