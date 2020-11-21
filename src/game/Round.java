package game;


import card.Card;

import java.util.*;

public class Round {
    public ArrayList<Card> cardDeck;
    private ArrayList<Player> activePlayers;
    private ArrayList<Card> faceUpCards;
    private Card firstCardRemoved = null;
    private Player currentPlayer;
    private int playerCount;

    public Round(Player firstplayer, ArrayList<Card> deck, ArrayList<Player> activePlayers){
        // carddeck should be created here otherwise we would get  error
        //remove() function cannot be called in removeDeckCard
        // secondly it would be better to create a deck and shuffle after each round
        //dealing should be also done here because after creating object of round all the things would be taken care of making, shuffing and dealing
        // delete this message

        this.cardDeck = deck;
        this.playerCount = 1;
        this.activePlayers = activePlayers;
        shuffleDeck();
        firstCardRemoved = pop();
        removeThreeMore(); //If(activePlayer.size() == 2) already in Method
    }

    /**
     * this method is called by the GameBoard to start a round after it is created
     */
    public void start() {

    }
    public void preTurn(Player player){
        this.currentPlayer = player;
        Card newCard = pop();
        //send message to player: "Its your turn!"
        //send message to player: "These are your cards: " + newCard + " " + player.currentCard"
    }

    public synchronized void handleTurn(Player player, Card card){
        //Player draws another card, which is also valid. Still missing here!
        if (player==this.currentPlayer && card==player.getCurrentCard()){
            //do turn
            //card needs to be discarded
            card.handleCard();
        }
        else{
            if (player!=this.currentPlayer){
                //send Message to player: "Please wait your turn!"
            }
            else{
                //send Message to player: "This card is not available."
            }

        }

        if(this.activePlayers.size()>=1){
            if(this.cardDeck.size()==0){
                //count card values and determine the winner, end round

            }
            else{
                //Nächsten Zug einleiten.
                preTurn(nextPlayer(player));

            }
        }
        else{
            //Beende Runde, da nurnoch ein Spieler im Spiel ist.
        }


    }
    /**
     * Shuffles the deck of Gameboard in each new round.
     */
    public ArrayList<Card> shuffleDeck(){
        Collections.shuffle(cardDeck);
        return cardDeck;
    }

    /**
     * removes first card of the deck
     * @return poped the removed card
     */
    public Card pop(){
        Card poped = cardDeck.get(0);
        cardDeck.remove(poped);
        return poped;
    }

    /**
     * removes three more cards from the deck, if there are only two players.
     * @return the three removed cards
     */
    public ArrayList<Card> removeThreeMore(){
        faceUpCards = new ArrayList<Card>();
         if(activePlayers.size() == 2){
             for(int i = 0; i<3; i++){
                 faceUpCards.add(pop()); //show?
             }
        }
        return faceUpCards;
    }

    /**
     * shows the three removed cards to the players
     */
    public void sendFaceUpCards(){
        //sendet abgedeckte Karten an alle Spieler
    }

    /**
     * deals out the first cards
     */
    public void dealCards(){
        for (Player player : activePlayers){
            player.setCurrentCard(pop());
        }
    }

    /**
     * lets players draw a card according to the order
     * @param activePlayers the ordered list of players
     */
    public void drawCards(ArrayList<Player> activePlayers){
        while (!isRoundFinished()){
            for (Player currentPlayer : activePlayers){
                currentPlayer.setSecondCard(pop());
                discardCards(currentPlayer);
            }
        }
    }

    /**
     * handles the discarding of a card.
     * @param currentPlayer the player who is in line to play a card
     */
    public void discardCards(Player currentPlayer){
        //remove old handmaid effect
        //currentPlayer.setPlayedHandmaid(false); 
        Card chosenCard = null;
        //if player has countess in hand check for prince or king
        if ( currentPlayer.getCurrentCard().value == Card.COUNTESS &&
                (currentPlayer.getSecondcard().value == Card.KING || currentPlayer.getSecondcard().value == Card.PRINCE)) {
            if (chosenCard == currentPlayer.getSecondcard()){
                //send Message to player: "You have to choose Countess, please try again."
            }
        } else if ( currentPlayer.getSecondcard().value == Card.COUNTESS &&
                (currentPlayer.getCurrentCard().value == Card.KING || currentPlayer.getCurrentCard().value == Card.PRINCE)) {
            if (chosenCard == currentPlayer.getCurrentCard()){
                //send Message to player: "You have to choose Countess, please try again."
            }
        }
        if (chosenCard == null) {
            //chosenCard = currentPlayer.chooseCardtoPlay();
            //handleCard(chosenCard);
        }
    }

    /**
     * Check if the round is finished
     * @return true when round is finished
     */
    public boolean isRoundFinished(){
        //A round ends if the deck is empty at the end of a player’s turn
        if (cardDeck.isEmpty()) return true;
        //A round also ends if all players but one are out of the round, in which case the remaining player wins
        if (activePlayers.size() < 2) return true; // one player has won
        return false;
    }

    /**
     * this methods returns the winner of the round
     * @return winner of the round
     */
    public ArrayList<Player> getRoundWinner() {
        ArrayList<Player> winnerList = new ArrayList<Player>();
        Player winner = activePlayers.get(0);
        //A round also ends if all players but one are out of the round, in which case the remaining player wins.
        if(activePlayers.size()==1){
            winnerList.add(winner);
            return winnerList;
        } else { //A round ends if the deck is empty at the end of a player’s turn
            for (int i = 1; i <= activePlayers.size(); i++) {
                //The player with the highest number in their hand wins the round.
                if (winner.getCurrentCard().getValue() < activePlayers.get(i).getCurrentCard().getValue()) {
                    winnerList.get(i); //TODO
                //In case of a tie, players add the numbers on the cards in their discard pile. The highest total wins.
                } else if (winner.getCurrentCard().getValue() == activePlayers.get(i).getCurrentCard().getValue()) {
                    if (winner.getCurrentCard().getValue() < activePlayers.get(i).getCurrentCard().getValue()) {
                        winner = activePlayers.get(i);//TODO
                    } else {
                        //
                    }
                }
            }
        }
        return winnerList;
    }
    /**
     * this method adds another player to the game.
     * @param player player that's supposed to be added.
     */
    public void addPlayer(Player player){
        if (this.playerCount <= 3) {
            this.activePlayers.add(player);
            this.playerCount += 1;
        }
        else{
            //send message to player: "The game is full, sorry!"
        }
    }
    /**
     * this method changes the currentPlayer attribute(which determines which player's turn it is).
     * @param player last player that played a card.
     */
    public Player nextPlayer(Player player){
        int temp = this.activePlayers.indexOf(player);
        Player next;
        if(temp < (playerCount-1)){
            next = this.activePlayers.get(temp + 1);
        }
        else{
            next = this.activePlayers.get(0);
        }
        return next;
    }
    /**
     * this method kicks one player from the game.
     * @param player player that will get kicked.
     */
    public void kickPlayer(Player player){
        this.activePlayers.remove(player);
    }
}
