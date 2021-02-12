package ai;

import game.gameObjects.maps.Map;
import game.gameObjects.tiles.*;
import utilities.Coordinate;
import utilities.enums.AttributeType;
import utilities.enums.CardType;
import utilities.enums.Orientation;

import java.util.ArrayList;

/**
 * This Class simulates game movement on the client side, in order to determine good moves for the AI.
 *
 * @author simon, Louis
 */

public class MoveSimulator {

    private final Map map;
    private final Coordinate controlPoint;
    private CardType[] cards;
    private Coordinate resPosition;
    private Orientation resOrientation;
    private boolean reboot = false;

    public MoveSimulator(AIClient aiClient, Map map, Coordinate controlpoint) {
        this.map = map;
        this.controlPoint = controlpoint;
    }

    /**
     * This method simulates game movement for one particular card combination.
     *
     * @param cards       card combination to be simulated
     * @param actualPos   actual position of the robot
     * @param orientation actual orientation of the robot
     */
    public Coordinate simulateCombination(CardType[] cards, Coordinate actualPos, Orientation orientation) {
        resPosition = actualPos.clone();
        resOrientation = orientation;
        this.cards = cards;

        for (int i = 0; i < 5; i++) {
            if (!reboot) {
                playCard(cards[i], i);
                activateBlueBelts();
                activateGreenBelts();
                activatePushPanel(i + 1);
                activateGear();
                //immediately interrupt if on controlpoint
                if (Coordinate.distance(resPosition, controlPoint) == 0) {
                    resPosition = controlPoint;
                    break;
                }
            } else {
                reboot = false;
                return null;
            }
        }
        return resPosition;
    }

    /**
     * Simulates one single card.
     *
     * @param card card to be simulated
     * @param i    current register(Required due to again card)
     */
    private void playCard(CardType card, int i) {

        switch (card) {
            case MoveI -> handleMove(resOrientation);
            case MoveII -> {
                handleMove(resOrientation);
                handleMove(resOrientation);
            }
            case MoveIII -> {
                handleMove(resOrientation);
                handleMove(resOrientation);
                handleMove(resOrientation);
            }
            case UTurn -> resOrientation = resOrientation.getOpposite();
            case TurnLeft -> resOrientation = resOrientation.getPrevious();
            case TurnRight -> resOrientation = resOrientation.getNext();
            case BackUp -> handleMove(resOrientation.getOpposite());
            case Again -> handleAgain(i);
        }
    }

    /**
     * Simulates the again card.
     *
     * @param i current register
     */
    private void handleAgain(int i) {
        if (i > 0) {
            playCard(cards[i - 1], i - 1);
        } else {
            reboot = true;
        }
    }

    /**
     * Simulates player movement for one tile in a specific direction.
     *
     * @param orientation orientation the robot should move in.
     */
    public void handleMove(Orientation orientation) {
        if (!reboot) {
            Coordinate newPos = resPosition.clone();
            newPos.add(orientation.toVector());

            boolean canMove = true;

            //look for a blocking wall on current Tile
            if (map.isWallBlocking(resPosition, orientation)) {
                canMove = false;
            }

            //look for a blocking wall on new tile
            if (!newPos.isOutsideMap()) {
                if (map.isWallBlocking(newPos, orientation.getOpposite())) {
                    canMove = false;
                }
            }

            if (canMove && !isRebooting(newPos)) {
                resPosition = newPos;
            }
        }
    }

    /**
     * Detects whether the player must reboot.
     *
     * @param newPos Position the player is on.
     */
    private boolean isRebooting(Coordinate newPos) {
        if (newPos.isOutsideMap()) {
            reboot = true;
            return true;
        } else for (Attribute a : map.getTile(newPos).getAttributes()) {
            if (a.getType() == AttributeType.Pit) {
                reboot = true;
                return true;
            }
        }
        return false;
    }

    /**
     * Simulates the green belts.
     */
    private void activateGreenBelts() {
        handleBeltMovement(map.getGreenBelts());
    }

    /**
     * Simulates the blue belts.
     */
    private void activateBlueBelts() {
        handleBeltMovement(map.getBlueBelts());
        handleBeltMovement(map.getBlueBelts());
    }

    /**
     * Executes belt movement in general.(Used by belt methods)
     *
     * @param belts List that includes coordinates of all belts that must be considered.(Either Green or Blue belt lists)
     */
    private void handleBeltMovement(ArrayList<Coordinate> belts) {
        Orientation orientation = null;
        boolean moved = false;
        for (Coordinate coordinate : belts) {
            if (coordinate.equals(resPosition) && !moved) {
                for (Attribute a : map.getTile(coordinate).getAttributes()) {
                    if (a instanceof Belt) {
                        orientation = ((Belt) a).getOrientation();
                    }
                    if (a instanceof RotatingBelt) {
                        orientation = ((RotatingBelt) a).getOrientations()[0];
                    }
                    handleMove(orientation);
                    moved = true;

                    //Rotate robot if moved on Rotating belt
                    for (Attribute attribute : map.getTile(resPosition).getAttributes()) {
                        if (attribute.getType() == AttributeType.RotatingBelt) {
                            RotatingBelt temp = ((RotatingBelt) attribute);
                            if (temp.getOrientations()[0] != orientation) rotateOnBelt(temp.getOrientations());
                        }
                    }
                }
            }
        }
    }

    /**
     * Simulates gears.
     */
    private void activateGear() {
        Gear gear = (Gear) map.getAttributeOn(AttributeType.Gear, resPosition);
        if (gear != null) {
            switch (gear.getOrientation()) {
                case LEFT -> resOrientation = resOrientation.getPrevious();
                case RIGHT -> resOrientation = resOrientation.getNext();
            }
        }
    }

    /**
     * Simulates push panels.
     */
    private void activatePushPanel(int i) {
        PushPanel pushPanel = (PushPanel) map.getAttributeOn(AttributeType.PushPanel, resPosition);
        if (pushPanel != null) {
            for (int register : pushPanel.getRegisters()) {
                if (i == register) {
                    handleMove(pushPanel.getOrientation());
                }
            }
        }
    }

    /**
     * Simulates belt rotations.
     */
    public void rotateOnBelt(Orientation[] o) {
        if (o[0].getNext() == o[1]) resOrientation = resOrientation.getNext();
        else if (o[0].getPrevious() == o[1]) resOrientation = resOrientation.getPrevious();
    }
}
