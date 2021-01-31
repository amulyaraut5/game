package ai;

import game.gameObjects.tiles.Attribute;
import game.round.ActivationPhase;
import utilities.Coordinate;
import utilities.enums.AttributeType;
import utilities.enums.CardType;
import utilities.enums.Orientation;

public class MoveSimulator {
    private AIClient aiClient;

    private Coordinate resPosition;
    private Orientation resOrientation;
    private boolean reboot = false;

    public MoveSimulator(AIClient aiClient) {
        this.aiClient = aiClient;
    }

    public Coordinate simulateCombination(CardType[] cards, Coordinate actualPos, Orientation orientation) {
        resPosition = actualPos.clone();
        resOrientation = orientation;

        for (int i = 0; i < 5; i++) {
            if (!reboot) {
                playCard(cards[i]);
            } else return actualPos;
//            activationElements.activateBlueBelts();
//            activationElements.activateGreenBelts();
//            activationElements.activatePushPanel();
//            activationElements.activateGear();
//            laserAction.activateBoardLaser(activePlayers);
//            laserAction.activateRobotLaser(activePlayers);
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
            if (ActivationPhase.isWallBlocking(resPosition, orientation, aiClient.getMap())) {
                canMove = false;
            }

            //look for a blocking wall on new tile
            if (!newPos.isOutsideMap()) {
                if (ActivationPhase.isWallBlocking(newPos, orientation.getOpposite(), aiClient.getMap())) {
                    canMove = false;
                }
            }
            if (canMove) resPosition = newPos;
            checkReboot();
        }
    }

    private void checkReboot() {
        if (resPosition.isOutsideMap()) {
            reboot = true;
        } else for (Attribute a : aiClient.getMap().getTile(resPosition).getAttributes()) {
            if (a.getType() == AttributeType.Pit) {
                reboot = true;
            }
        }
    }
}
