package game.round;

import game.Player;
import game.gameActions.RebootAction;
import game.gameObjects.cards.Card;
import game.gameObjects.cards.damage.Spam;
import game.gameObjects.cards.damage.Trojan;
import game.gameObjects.cards.damage.Virus;
import game.gameObjects.cards.damage.Worm;
import game.gameObjects.decks.*;
import game.gameObjects.robot.Robot;
import game.gameObjects.tiles.Attribute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.User;
import utilities.Coordinate;
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.*;
import utilities.RegisterCard;
import utilities.enums.*;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * The Activation Phase is the third phase in the Round.
 * In this class the Programming Cards and GameBoard Tiles are activated.
 *
 * @author janau, sarah, annika, Louis
 */

public class ActivationPhase extends Phase {

    private static final Logger logger = LogManager.getLogger();

    /**
     * The players and their cards for the current register
     */
    private final ArrayList<RegisterCard> currentCards = new ArrayList<>();
    /**
     * saves the cardTypes of damageCards
     */
    private final ArrayList<CardType> cardTypes = new ArrayList<>();

    private final BoardElements activationElements = new BoardElements(this);
    private final LaserAction laserAction = new LaserAction();

    private final ArrayList<Player> rebootedPlayers = new ArrayList<>();

    private final SpamDeck spamDeck = game.getSpamDeck();
    private final TrojanDeck trojanDeck = game.getTrojanDeck();
    private final VirusDeck virusDeck = game.getVirusDeck();
    private final WormDeck wormDeck = game.getWormDeck();

    /**
     * keeps track of the current register
     */
    private int currentRegister = 1;

    /**
     * starts the ActivationPhase.
     * After each register the method for activating the board tiles ist called.
     * In every register the priority is determined (-> turnCards) and the players cards get activated
     * in priority order.
     */
    public ActivationPhase() {
        super();
        turnCards(currentRegister);
    }

    /**
     * At the beginning of each register the cards of each player in the current register is shown.
     * This is already in priority order.
     */
    public void turnCards(int register) {
        for (Player player : determinePriority(map.getAntenna())) {
            logger.info("RegisterCards of Player " + player.getName() + ": " + player.getRegisterCards());
            Card card = player.getRegisterCard(register);
            if (!(card == null)) {
                RegisterCard playerRegisterCard = new RegisterCard(player.getID(), card);
                currentCards.add(playerRegisterCard);
            } else {
                game.getActivePlayers().remove(player);
            }
        }
        if (!(currentCards.isEmpty())) {
            server.communicateAll(new CurrentCards(currentCards));
            server.communicateAll(new CurrentPlayer((currentCards.get(0)).getPlayerID()));
        }
    }

    /**
     * This method is called whenever a PlayIt() protocol is received.
     * it is checked if its the given players turn. If yes, the cards is handled.
     */
    public void activateCards(int playerID) {

        //Because currentCards is in priority order the first person to activate their cards is at index 0.
        //So by removing the index 0 after every players turn the current player is always at index 0.

        RegisterCard playerRegisterCard = currentCards.get(0);

        //if its this players turn his card is activated
        outerLoop:
        if (playerRegisterCard.getPlayerID() == playerID) {
            CardType currentCard = playerRegisterCard.getCard();
            handleCard(currentCard, game.getPlayerFromID(playerID));
            if (!currentCards.isEmpty()) {
                currentCards.remove(0);
            }
            //check if next player is rebooting
            if (!currentCards.isEmpty()) {
                while (isRebooting(game.getPlayerFromID(currentCards.get(0).getPlayerID()))) {
                    currentCards.remove(0);
                    if (currentCards.isEmpty()) {
                        break;
                    }
                }
            }

            //if he was the last player to send the PlayIt() protocol for this register the board is activated
            if (activePlayers.isEmpty()) {
                activePlayers.addAll(rebootedPlayers);
                rebootedPlayers.clear();
                game.nextPhase();
                break outerLoop;
            }
            if (currentCards.isEmpty()) {
                handleFinishedRegister();
            } else {
                server.communicateAll(new CurrentPlayer((currentCards.get(0)).getPlayerID()));
                logger.info(rebootedPlayers);
            }
        } else { //if the player at index 0 is not the player that send the PlayIt() protocol he gets an error
            server.communicateDirect(new Error("It is not your turn!"), playerID);
            //
        }
    }

    /**
     * Checkpoint is the final destination of the game and player wins the game as
     * soon as the player has reached all the checkpoints in numerical order.
     * The player gets the checkpoint token.
     * CheckPointReached and GameWon Protocol are sent.
     */
    public void activateControlPoint() {
        outerLoop:
        for (Coordinate coordinate : map.readControlPointCoordinate()) {
            for (Attribute a : map.getTile(coordinate).getAttributes()) {
                if (a.getType() == AttributeType.ControlPoint) {
                    int checkPointID = ((game.gameObjects.tiles.ControlPoint) a).getCount();
                    int totalCheckPoints = map.readControlPointCoordinate().size();
                    for (Player player : players) {
                        if (player.getRobot().getCoordinate().equals(coordinate)) {
                            if (player.getCheckPointCounter() == (checkPointID - 1)) {
                                if (checkPointID < totalCheckPoints) {
                                    int checkPoint = player.getCheckPointCounter();
                                    checkPoint++;
                                    player.setCheckPointCounter(checkPoint);
                                    server.communicateAll(new CheckpointReached(player.getID(), checkPointID));
                                    player.message(new Error("Congratulations: You have reached " + checkPointID + " checkPoint"));
                                } else if (checkPointID == totalCheckPoints) {
                                    server.gameWon(player.getID());
                                    break outerLoop;
                                }
                            } else if (player.getCheckPointCounter() > checkPointID) {
                                player.message(new Error("CheckPoint Already Reached"));
                            } else {
                                player.message(new Error("You need to go CheckPoint " + (player.getCheckPointCounter() + 1) + " first"));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * this method starts the next register if the game is not already won or all players are rebooting.
     * If it's already the fifth register the next Phase is called.
     */

    public void handleFinishedRegister() {
        if (server.getServerState() == ServerState.RUNNING_GAME) {
            activateBoard();
            if (currentRegister < 5) { //if it is not the 5th register yet the cards from the next register are turned
                currentRegister++;
                logger.info("Now in Register " + currentRegister);
                if (!allPlayersRebooting()) turnCards(currentRegister);
                else {
                    game.nextPhase();
                    logger.info("Next Phase called bcs all Rebooting");
                }
            } else { //if it is already the 5th register the next phase is called
                if (!rebootedPlayers.isEmpty()) {
                    activePlayers.addAll(rebootedPlayers);
                    rebootedPlayers.clear();
                }
                game.nextPhase();
            }
        }
    }

    /**
     * removes the current card of a player that has disconnected from the server.
     *
     * @param playerID player who disconnected
     */

    public void removeCurrentCards(int playerID) {
        RegisterCard temp = null;
        for (RegisterCard rc : currentCards) {
            if (rc.getPlayerID() == playerID) {
                temp = rc;
            }
        }
        currentCards.remove(temp);
        Player player = game.getPlayerFromID(playerID);
        activePlayers.remove(player);
        rebootedPlayers.remove(player);
        if (currentCards.isEmpty() && currentRegister < 5) {
            handleFinishedRegister();
        } else {
            server.communicateAll(new CurrentPlayer((currentCards.get(0)).getPlayerID()));
        }
    }

    /**
     * Method that activates the board elements in their right order.
     */
    public void activateBoard() {
        activationElements.activateBlueBelts();
        activationElements.activateGreenBelts();
        activationElements.activatePushPanel();
        activationElements.activateGear();
        server.communicateAll(new PlayerShooting());
        laserAction.activateBoardLaser(activePlayers);
        laserAction.activateRobotLaser(activePlayers);
        activationElements.activateEnergySpace();
        activateControlPoint();
    }

    /**
     * Handles the movment of one player for one tile in a specific direction. Considers board elements.
     *
     * @param player Player that is moved
     * @param o      Orientation player is moved to
     */
    public void handleMove(Player player, Orientation o) {
        //calculate potential new position
        Coordinate newPosition = activationElements.calculateNew(player, o);
        boolean canMove = true;

        //look for a blocking wall on current Tile
        if (map.isWallBlocking(player.getRobot().getCoordinate(), o)) {
            canMove = false;
        }
        //look for a blocking wall on new tile
        if (!newPosition.isOutsideMap()) {
            if (map.isWallBlocking(newPosition, o.getOpposite())) {
                canMove = false;
            }
        }
        //Handle collisions
        for (Player collisionPlayer : players) {
            if (newPosition.equals(collisionPlayer.getRobot().getCoordinate())) {
                Coordinate old = collisionPlayer.getRobot().getCoordinate().clone();
                handleMove(collisionPlayer, o);
                if ((old.equals(collisionPlayer.getRobot().getCoordinate()))) {
                    canMove = false;
                }
            }
        }
        //move robot, activate board element if given
        if (canMove) moveOne(player, o);
    }

    /**
     * Moves the player in a direction. Does not consider board elements. Only called by the handleMove method.
     *
     * @param player      Player that is moved
     * @param orientation Orientation player is moved to
     */
    public void moveOne(Player player, Orientation orientation) {
        player.getRobot().move(orientation);
        handleTile(player);
    }

    public void handleCard(CardType cardType, Player player) {
        Robot robot = player.getRobot();
        Orientation orientation = robot.getOrientation();

        switch (cardType) {
            case MoveI -> handleMove(player, orientation);
            case MoveII -> {
                handleMove(player, orientation);
                if (!isRebooting(player)) handleMove(player, orientation);
            }
            case MoveIII -> {
                handleMove(player, orientation);
                if (!isRebooting(player)) handleMove(player, orientation);
                if (!isRebooting(player)) handleMove(player, orientation);
            }
            case TurnLeft -> robot.rotate(Rotation.LEFT);

            case TurnRight -> robot.rotate(Rotation.RIGHT);

            case UTurn -> robot.rotateTo(orientation.getOpposite());

            case BackUp -> handleMove(player, orientation.getOpposite());

            case PowerUp -> {
                player.setEnergyCubes(player.getEnergyCubes() + 1);
                server.communicateAll(new Energy(player.getID(), 1));
            }
            case Again -> handleRecursion(player);

            case Spam -> {
                //Add spam card back into the spam deck
                spamDeck.addCard(new Spam());
                //remove top card from programming deck
                if (player.getDrawProgrammingDeck().isEmpty()) {
                    player.reuseDiscardedDeck();
                }
                replaceDamageCard(player);
            }
            case Worm -> {
                //Reboot the robot.
                new RebootAction().doAction(player);
                RegisterCard toRemove = null;
                for (RegisterCard registerCard : currentCards) {
                    if (registerCard.getPlayerID() == player.getID()) {
                        toRemove = registerCard;
                    }
                }
                currentCards.remove(toRemove);
                //Add worm card back into the worm deck
                wormDeck.getDeck().add(new Worm());
            }
            case Virus -> {
                int robotX = robot.getCoordinate().getX();
                int robotY = robot.getCoordinate().getY();
                for (Player otherPlayer : players) {
                    int otherRobotX = otherPlayer.getRobot().getCoordinate().getX();
                    int otherRobotY = otherPlayer.getRobot().getCoordinate().getY();

                    if (otherPlayer != player && (otherRobotX <= robotX + 6 || otherRobotY <= robotY + 6)) {
                        drawDamage(virusDeck, otherPlayer, 1);
                    }
                }
                virusDeck.addCard(new Virus());
                //remove top card from programming deck
                if (player.getDrawProgrammingDeck().isEmpty()) {
                    player.reuseDiscardedDeck();
                }
                replaceDamageCard(player);
            }
            case Trojan -> {
                trojanDeck.addCard(new Trojan());
                //Draw two spam cards
                drawDamage(spamDeck, player, 2);
                if (player.getDrawProgrammingDeck().isEmpty()) {
                    player.reuseDiscardedDeck();
                }
                replaceDamageCard(player);
            }
            default -> logger.error("The CardType " + cardType + " is invalid or not yet implemented!");
        }
        activateControlPoint();
    }

    /**
     * this method checks if the top card of the draw deck is an Again card
     *
     * @param player player who has to draw a card
     */

    public void replaceDamageCard(Player player) {
        for (Card card : player.getDrawProgrammingDeck().getDeck()) {
            if (!(currentRegister == 1 && card.getName() == CardType.Again)) {
                Card topCard = player.getDrawProgrammingDeck().popThisCard(card);
                player.setRegisterCards(currentRegister, card);
                handleCard(topCard.getName(), player);
                break;
            }
        }
    }

    /**
     * This method handles recursion for Again Card with different base cases. If the player plays again card back to
     * back in 2 registers, then in such case the card from the {register-2} is used.
     * Under normal circumstance the effect of immediate previous card is handled.
     *
     * @param player current player playing game
     */

    public void handleRecursion(Player player) {
        if (currentRegister == 1)
            player.message(new Error("No Previous Movement Recorded"));
        else if (currentRegister == 2 && player.getLastRegisterCard() == CardType.Again)
            player.message(new Error("I am an Idiot."));
        else {
            if (player.getLastRegisterCard() == CardType.Again) {
                int currentRegister = getCurrentRegister();
                Card card = player.getRegisterCard(currentRegister - 2);
                handleCard(card.getName(), player);
            } else {
                handleCard(player.getLastRegisterCard(), player);
            }
        }
    }

    /**
     * Handles effects of a specific position on the map.(Outside the map? Did the player fall into a pit?)
     *
     * @param player Player whose tile must be checked
     */
    public void handleTile(Player player) {
        Coordinate robotPos = player.getRobot().getCoordinate();
        if (robotPos.isOutsideMap() || map.getAttributeOn(AttributeType.Pit, robotPos) != null) {
            rebootedPlayers.add(player);
            activePlayers.remove(player);
            new RebootAction().doAction(player);
            RegisterCard toRemove = null;
            for (RegisterCard registerCard : currentCards) {
                if (registerCard.getPlayerID() == player.getID()) {
                    toRemove = registerCard;
                }
            }
            currentCards.remove(toRemove);
        }
    }

    /**
     * It checks whether the position is free or not.
     *
     * @param position position of tile on board
     * @return true if no robot is on that tile
     */
    public boolean isPositionFree(int position) {
        Coordinate coordinate = Coordinate.parse(position);
        for (Player collisionPlayer : players) {
            if (coordinate.equals(collisionPlayer.getRobot().getCoordinate())) {
                return false;
            }
        }
        return true;
    }

    /**
     * It checks whether the position is free or not.
     *
     * @param coordinate coordinate of tile on board
     * @return true if no robot is on that tile
     */
    public boolean isPositionFree(Coordinate coordinate) {
        for (Player collisionPlayer : players) {
            if (coordinate.equals(collisionPlayer.getRobot().getCoordinate())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines the priority depending on the antenna and returns a list of players in order of priority.
     *
     * @param antenna game antenna
     * @return a list of players in priority order
     */
    public ArrayList<Player> determinePriority(Coordinate antenna) {
        ArrayList<RobotDistance> sortedDistance = sortDistance(antenna);
        ArrayList<Player> playerPriority = new ArrayList<>();
        int sortedDistanceSize = sortedDistance.size();

        for (int i = 0; i < sortedDistanceSize; i++) {
            if (sortedDistance.size() == 0) {
                return playerPriority;
            } else if (sortedDistance.size() == 1) {
                playerPriority.add(sortedDistance.get(0).getPlayer());
                sortedDistance.remove(0);
                //objects have the same distance values -> selection by clockwise antenna beam
            } else if (sortedDistance.get(0).getDistance() == sortedDistance.get(1).getDistance()) {
                //add the robots with same distance into a list
                ArrayList<RobotDistance> sameDistance = new ArrayList<>();
                RobotDistance firstSameDistance = sortedDistance.get(0);
                int tempSortedDistanceSize = sortedDistance.size();
                //compare first element with same distance with all following elements and add matching ones to list sameDistance
                for (int k = 0; k < tempSortedDistanceSize; k++) {
                    if (firstSameDistance.getDistance() == sortedDistance.get(0).getDistance()) {
                        sameDistance.add(sortedDistance.get(0));
                        sortedDistance.remove(sortedDistance.get(0));
                    }
                }
                sortByYCoordinate(sameDistance, antenna, playerPriority);
                //first and second object have different distance values -> first player in list is currentPlayer
            } else {
                playerPriority.add(sortedDistance.get(0).getPlayer());
                sortedDistance.remove(0);
            }
        }
        return playerPriority;
    }

    /**
     * Sorts the list of players with the same distance to the antenna
     * by the order in which the antenna beam hits them clockwise.
     *
     * @param sameDistance   ArrayList of robots with the same distance
     * @param antenna        game antenna
     * @param playerPriority a list of players in priority order
     */
    public void sortByYCoordinate(ArrayList<RobotDistance> sameDistance, Coordinate antenna, ArrayList<Player> playerPriority) {
        sameDistance.sort(Comparator.comparingInt(RobotDistance::getYCoordinate));
        ArrayList<Player> greaterThanAntenna = new ArrayList<>();
        ArrayList<Player> smallerThanAntenna = new ArrayList<>();

        for (RobotDistance rd : sameDistance) {
            int antennaY = antenna.getY();
            int robotY = rd.getRobot().getCoordinate().getY();
            if (robotY < antennaY) {
                smallerThanAntenna.add(rd.getPlayer());
            } else if (robotY > antennaY) {
                greaterThanAntenna.add(rd.getPlayer());
            } else {
                playerPriority.add(rd.getPlayer());
            }
        }
        playerPriority.addAll(greaterThanAntenna);
        playerPriority.addAll(smallerThanAntenna);
    }

    /**
     * Fills the ArrayList sortedDistance with matching objects
     * and sorts them according to their distance to the antenna.
     *
     * @param antenna the game antenna
     * @return an ArrayList of players and their robots sorted by their distance to the antenna
     */
    public ArrayList<RobotDistance> sortDistance(Coordinate antenna) {
        ArrayList<RobotDistance> sortedDistance = new ArrayList<>();

        int i = 0;
        while (i < activePlayers.size()) {
            Coordinate robotPosition = new Coordinate(
                    activePlayers.get(i).getRobot().getCoordinate().getX(),
                    activePlayers.get(i).getRobot().getCoordinate().getY());

            Player player = activePlayers.get(i);
            Robot robot = activePlayers.get(i).getRobot();
            double distance = Coordinate.distance(antenna, robotPosition);
            int yRobot = robot.getCoordinate().getY();

            sortedDistance.add(new RobotDistance(player, robot, distance, yRobot));
            i++;
        }
        sortedDistance.sort(Comparator.comparingDouble(RobotDistance::getDistance));
        return sortedDistance;
    }

    /**
     * whenever a robot receives damage this method is called to check if there are enough damage cards available
     * and send drawDamage or PickDamage based on this information.
     *
     * @param damageDeck Deck from which the damage cards should be drawn
     * @param player     player whose robot received damage
     * @param amount     number of damage cards to draw
     */

    public void drawDamage(Deck damageDeck, Player player, int amount) {
        logger.info("drawDamage reached");
        cardTypes.clear();
        logger.info("Vorher : (Decks) Spam: " + game.getSpamDeck().size() + " Trojan: " + game.getTrojanDeck().size() +
                " Virus: " + game.getVirusDeck().size() + " Worm: " + game.getWormDeck().size());
        if (!(damageDeck.size() < amount)) {
            ArrayList<Card> damageCards = damageDeck.drawCards(amount);
            player.getDiscardedProgrammingDeck().getDeck().addAll(damageCards);
            for (Card card : damageCards) {
                cardTypes.add(card.getName());
            }
            logger.info("Nachher: (Decks) Spam: " + game.getSpamDeck().size() + " Trojan: " + game.getTrojanDeck().size() +
                    " Virus: " + game.getVirusDeck().size() + " Worm: " + game.getWormDeck().size());
            server.communicateAll(new DrawDamage(player.getID(), cardTypes));
        } else {
            if (damageDeck.size() == 0) {
                server.communicateDirect(new PickDamage(amount), player.getID());
            } else {
                int alreadyDrawn = damageDeck.size();
                ArrayList<Card> damageCards = damageDeck.drawCards(alreadyDrawn);
                for (Card card : damageCards) {
                    cardTypes.add(card.getName());
                }
                player.getDiscardedProgrammingDeck().getDeck().addAll(damageCards);
                server.communicateDirect(new PickDamage(amount - alreadyDrawn), player.getID());
            }
        }
    }

    /**
     * If a selectDamage protocol was received this method tries to draw the cards from the chosen decks.
     * If there are not enough cards PickDamage is sent again
     *
     * @param selectDamage received message
     * @param user         user whose robot receives damage
     */
    public void handleSelectedDamage(SelectDamage selectDamage, User user) {
        logger.info("handleSelectedDamage");
        Player player = game.userToPlayer(user);
        ArrayList<CardType> selectedCards = selectDamage.getCards();
        logger.info("selectedCards Damage. " + selectedCards);
        int drawAgain = 0;
        for (CardType cardType : selectedCards) {
            if (!(cardType == null)) {
                switch (cardType) {
                    case Spam -> {
                        if (!(spamDeck.isEmpty())) {
                            player.getDiscardedProgrammingDeck().addCard(new Spam());
                            spamDeck.pop();
                        } else {
                            drawAgain++;
                        }
                    }
                    case Virus -> {
                        if (!(virusDeck.isEmpty())) {
                            player.getDiscardedProgrammingDeck().addCard(new Virus());
                            virusDeck.pop();
                        } else {
                            drawAgain++;
                        }
                    }
                    case Worm -> {
                        if (!(virusDeck.isEmpty())) {
                            player.getDiscardedProgrammingDeck().addCard(new Worm());
                            wormDeck.pop();
                        } else {
                            drawAgain++;
                        }
                    }
                    case Trojan -> {
                        if (!(virusDeck.isEmpty())) {
                            player.getDiscardedProgrammingDeck().addCard(new Trojan());
                            trojanDeck.pop();
                        } else {
                            drawAgain++;
                        }
                    }
                    default -> player.message(new Error("This is not a valid damage card"));
                }
                cardTypes.add(cardType);
            }
            if (drawAgain == 0) {
                logger.info("(Decks) Spam: " + game.getSpamDeck().size() + " Trojan: " + game.getTrojanDeck().size() +
                        " Virus: " + game.getVirusDeck().size() + " Worm: " + game.getWormDeck().size());
                server.communicateAll(new DrawDamage(user.getID(), cardTypes));
                logger.info("playerDiscard: " + player.getDiscardedProgrammingDeck().getDeck());
            } else {
                server.communicateDirect(new PickDamage(drawAgain), player.getID());
            }
        }
    }

    /**
     * Checks if every player is rebooting at the same time.
     */
    public boolean allPlayersRebooting() {
        boolean allRebooting = true;
        for (Player player : players) {
            if (!isRebooting(player)) allRebooting = false;
        }
        return allRebooting;
    }

    /**
     * returns if a player is not rebooting
     *
     * @param player player to check
     * @return true if player is rebooting
     */

    public boolean isRebooting(Player player) {
        boolean isRebooting = false;
        for (Player p : rebootedPlayers) {
            if (player == p) {
                isRebooting = true;
                break;
            }
        }
        return isRebooting;
    }

    /**
     * This is independent of activated board method and used for cheats.
     * #fire activates this method.
     */

    public void activateCheatLaser() {
        laserAction.activateRobotLaser(activePlayers);
        laserAction.activateBoardLaser(activePlayers);
        server.communicateAll(new PlayerShooting());
    }

    public ArrayList<RegisterCard> getCurrentCards() {
        return currentCards;
    }

    public int getCurrentRegister() {
        return currentRegister;
    }

    public BoardElements getActivationElements() {
        return activationElements;
    }

    /**
     * Class to handle the players robots by y-coordinate and distance from antenna
     */
    public static class RobotDistance {
        private final Player player;
        private final Robot robot;
        private final double distance;
        private final int yCoordinate;

        public RobotDistance(Player player, Robot robot, double distance, int yCoordinate) {
            this.player = player;
            this.robot = robot;
            this.distance = distance;
            this.yCoordinate = yCoordinate;
        }

        public Player getPlayer() {
            return player;
        }

        public Robot getRobot() {
            return robot;
        }

        public double getDistance() {
            return distance;
        }

        public int getYCoordinate() {
            return yCoordinate;
        }
    }
}