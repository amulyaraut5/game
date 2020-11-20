package game;


import card.Card;

import java.util.*;

public class Round {
    private ArrayList<Player> activePlayers;
    private Map<Player, List<Card>> playedCards;
    public ArrayList<Card> cardDeck; //Remove after getStackCards is created
    private ArrayList<Card> faceUpCards;
    public Stack <Card> cardDeck;
    private Stack <Card> faceUpCards;
    private Card firstCardRemoved = null;
    private Player currentPlayer;
    private int playerCount;

    public Round(Player firstplayer, Stack<Card> deck, ArrayList<Player> activePlayers){
        // carddeck should be created here otherwise we would get  error
        //remove() function cannot be called in removeDeckCard
        // secondly it would be better to create a deck and shuffle after each round
        //dealing should be also done here because after creating object of round all the things would be taken care of making, shuffing and dealing
        // delete this message
        this.cardDeck = deck;
        this.playerCount = 1;
        this.activePlayers=activePlayers;
        shuffleDeck(cardDeck);
        firstCardRemoved = pop();
        if(activePlayers.size()==2){
            removeThreeMore(cardDeck);
        }

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
    public Stack<Card> shuffleDeck(Stack<Card> cardDeck){
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
     * @param cardDeck the created cardeck
     * @return the three removed cards
     */
    public Stack<Card> removeThreeMore(Stack<Card> cardDeck){
        faceUpCards = new Stack<Card>();
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

    public void drawCards(){

    }

    public void discardCards(){

    }

    /**
     * Number of players that are not out yet.
     *  @return number of players that are still in the game
     */
    public int numPlayerStillInRound(){ //Needs inGame-method in Player
        int number = 0;
        return number;
    }

    /**
     * Check if the round is finished
     * @return true when round is finished
     */
    public boolean isRoundFinished(){
        if (cardDeck.size() <= 1) return true;         // last card has been drawn (none or one card left in stack/list)
        if (numPlayerStillInRound() < 2) return true; // one player has won
        return false;

    }


    /**
     * this methods returns the winner of the round
     * @return winner of the round
     */
    public Player getRoundWinner() {
        Player winner = null;
        return winner;
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
