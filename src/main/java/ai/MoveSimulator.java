package ai;

import game.gameObjects.maps.Map;
import game.gameObjects.tiles.*;
import game.round.ActivationPhase;
import utilities.Coordinate;
import utilities.enums.AttributeType;
import utilities.enums.CardType;
import utilities.enums.Orientation;

import java.util.ArrayList;

public class MoveSimulator {
    private AIClient aiClient;

    private Coordinate resPosition;
    private Orientation resOrientation;
    private boolean reboot = false;
    private Map map;

    public MoveSimulator(AIClient aiClient, Map map) {
        this.aiClient = aiClient;
        this.map = map;
    }

    public Coordinate simulateCombination(CardType[] cards, Coordinate actualPos, Orientation orientation) {
        resPosition = actualPos.clone();
        resOrientation = orientation;

        for (int i = 0; i < 5; i++) {
            if (!reboot) {
                playCard(cards[i]);
                activateBlueBelts();
                activateGreenBelts();
                //activatePushPanel();
                activateGear();
            } else {
                reboot = false;
                return actualPos;
            }
        }
        return resPosition;
    }

    private void activatePushPanel() {
        PushPanel pushPanel = (PushPanel) map.getAttributeOn(AttributeType.PushPanel, resPosition);
        if (pushPanel != null) {
            //TODO implement
        }
    }

    private void activateGear() {
        Gear gear = (Gear) map.getAttributeOn(AttributeType.Gear, resPosition);
        if (gear != null) {
            switch (gear.getOrientation()) {
                case LEFT -> resOrientation = resOrientation.getPrevious();
                case RIGHT -> resOrientation = resOrientation.getNext();
            }
        }
    }

    private void playCard(CardType card) {

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
            case UTurn -> {
                resOrientation = resOrientation.getOpposite();
            }
            case TurnLeft -> {
                resOrientation = resOrientation.getPrevious();
            }
            case TurnRight -> {
                resOrientation = resOrientation.getNext();
            }
            case BackUp -> {
                handleMove(resOrientation.getOpposite());
            }
            //TODO case Again -> handleRecursion(player, orientation);
        }
    }

    public void handleMove(Orientation orientation) {
        if (!reboot) {
            Coordinate newPos = resPosition.clone();
            newPos.add(orientation.toVector());

            boolean canMove = true;

            //look for a blocking wall on current Tile
            if (ActivationPhase.isWallBlocking(resPosition, orientation, map)) {
                canMove = false;
            }

            //look for a blocking wall on new tile
            if (!newPos.isOutsideMap()) {
                if (ActivationPhase.isWallBlocking(newPos, orientation.getOpposite(), map)) {
                    canMove = false;
                }
            }
            if (!isRebooting(newPos)) {
                resPosition = newPos;
            }
        }
    }

    private boolean isRebooting(Coordinate newPos) {
        boolean isRebooting = false;

        if (newPos.isOutsideMap()) {
            reboot = true;
            isRebooting = true;
        } else for (Attribute a : map.getTile(newPos).getAttributes()) {
            if (a.getType() == AttributeType.Pit) {
                reboot = true;
                isRebooting = true;
            }
        }
        return isRebooting;
    }

    private void activateGreenBelts() {
        handleBeltMovement(map.getGreenBelts());
    }

    private void activateBlueBelts() {
        handleBeltMovement(map.getBlueBelts());
        handleBeltMovement(map.getBlueBelts());
    }


    private void handleBeltMovement(ArrayList<Coordinate> belts) {
        Orientation orientation = null;
        for (Coordinate coordinate : belts) {
            if (coordinate.equals(resPosition)) {
                for (Attribute a : map.getTile(coordinate).getAttributes()) {
                    if (a.getType() == AttributeType.Belt) {
                        orientation = ((Belt) a).getOrientation();
                    }
                    if (a.getType() == AttributeType.RotatingBelt) {
                        orientation = ((RotatingBelt) a).getOrientations()[0];
                    }

                    handleMove(orientation);
                }
            }
        }
    }


}
