package game.round;

import game.Game;
import game.Player;
import game.gameActions.MoveRobot;
import game.gameActions.PowerUpRobot;
import game.gameActions.RebootAction;
import game.gameObjects.maps.Map;
import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Tile;
import utilities.Coordinate;
import utilities.JSONProtocol.JSONBody;
import utilities.JSONProtocol.body.Energy;
import utilities.Orientation;

import java.util.ArrayList;

public class ActivationElements {
    Game game = Game.getInstance();
    ArrayList<Player> playerList = game.getPlayers();
    Map map = game.getMap();


    /**
     * Checkpoint is the final destination of the game and player wins the game as
     * soon as the player has reached all the checkpoints in chronological order.
     * The player gets the checkpoint token.
     *
     */

    public void activateControlPoint(){

    }

    /**
     * Whenever the player finds in this tile, player gets the energy token.
     *
     */

    public void activateEnergySpace(){
        for(Coordinate coordinate: map.getEnergySpaceCoordinate()){
            for(Player player: playerList){
                if (player.getRobot().getPosition().getX() == coordinate.getX()
                        && player.getRobot().getPosition().getY() == coordinate.getY()) {

                    int energy = player.getEnergyCubes();
                    energy += energy;
                    player.setEnergyCubes(energy);
                    // Todo Decrease the energy cube number

                    JSONBody jsonBody = new Energy(player.getId(), player.getEnergyCubes());
                    player.message(jsonBody);

                }
            }
        }
    }

    /**
     * Push panels push any robots resting on them into the next space in the direction the push
     * panel faces. They activate only in the register that corresponds to the number on them. For
     * example, if you end register two on a push panel labeled “2, 4” you will be pushed. If you end
     * register three on the same push panel, you won’t be pushed.
     */
    /*
    public void activatePushPanel(){
        for(Coordinate coordinate: map.getPushPanelCoordinate()){
            Tile tile = map.getTile(coordinate);
            for(Player player: playerList){
                if (player.getRobot().getPosition().getX() == coordinate.getX()
                        && player.getRobot().getPosition().getY() == coordinate.getY()) {
                    for(Attribute a : tile.getAttributes()){
                        for(int i : ((game.gameObjects.tiles.PushPanel) a).getRegisters()){
                            if( i == player.getCurrentRegister()){
                                ///new MoveRobot().doAction(a.getOrientation(), player);
                            }
                        }
                    }
                }
            }
        }
    }

     */

}