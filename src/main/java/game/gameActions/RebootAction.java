package game.gameActions;

import game.Player;
import game.gameObjects.cards.Card;
import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Tile;
import utilities.Coordinate;
import utilities.Orientation;
import utilities.Utilities;

/**
 * @author annika
 */
public class RebootAction extends Action{

    /**
     * If the robot falls off the board or into a pit, or if a worm card is activated,
     * the robot must be rebooted.
     * Note: If multiple robots reboot on the same board in the same round or if a robot sits on the reboot token when other robots are rebooting, robots will leave the reboot space in the order they rebooted, with the next robot pushing the robot before it in the direction indicated by the arrow on the reboot token.
     * @param orientation
     * @param player is the player who is affected by the game action.
     */
    @Override
    public void doAction(Orientation orientation, Player player) {
        //Draw two spam cards
        for (int i = 0; i < 2; i++) {
            Card spamCard = player.getGame().getSpamDeck().pop();
            player.getDiscardedProgrammingDeck().getDeck().add(spamCard);
        }
        //discard cards in registers on discard pile
        player.getRegisterCards().addAll(player.getDiscardedProgrammingDeck().getDeck());
        player.getRegisterCards().clear();
        //Out of the round, must wait until the next round to program the robot again.
        //game.getActiveRound().getPlayerList().remove(player);
        player.freeze();
        //Robot is placed on reboot token
        Tile[][] currentMap = player.getGame().getMap().getTiles();
        player.getRobot().setPosition(getRebootTileCoordinates(currentMap));
        //TODO The player can turn the robot to face any direction.
    }


    public Coordinate getRebootTileCoordinates(Tile[][] map) {
        for (int i = 0; i < (map.length); i++) {
            for (int j = 0; j < (map[0].length); j++) {
                for (Attribute a : map[i][j].getAttributes()) {
                    if (a.getType() == Utilities.AttributeType.Reboot) {
                        return new Coordinate(i, j);
                    }
                }
            }
            break;
        }
        return null;
    }
}
