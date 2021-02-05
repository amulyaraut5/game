package ai;

import game.gameObjects.maps.Map;
import game.gameObjects.tiles.*;
import utilities.Coordinate;
import utilities.enums.AttributeType;
import utilities.enums.CardType;
import utilities.enums.Orientation;

import java.util.ArrayList;

public class MoveSimulator {
    private final AIClient aiClient;
    private final Map map;
    private Coordinate resPosition;
    private Orientation resOrientation;
    private boolean reboot = false;

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
                activatePushPanel(i + 1);
                activateGear();
            } else {
                reboot = false;
                return actualPos;
            }
        }
        return resPosition;
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
            case UTurn -> resOrientation = resOrientation.getOpposite();
            case TurnLeft -> resOrientation = resOrientation.getPrevious();
            case TurnRight -> resOrientation = resOrientation.getNext();
            case BackUp -> handleMove(resOrientation.getOpposite());
            //TODO case Again -> handleRecursion(player, orientation);
        }
    }

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
                    if (a instanceof Belt) {
                        orientation = ((Belt) a).getOrientation();
                    }
                    if (a instanceof RotatingBelt) {
                        orientation = ((RotatingBelt) a).getOrientations()[0];
                    }
                    handleMove(orientation);
                }
            }
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
}
