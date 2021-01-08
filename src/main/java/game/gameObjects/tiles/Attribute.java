package game.gameObjects.tiles;

import client.model.Client;
import game.Game;
import game.Player;
import javafx.scene.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.UserThread;
import utilities.Orientation;

/**
 * @author Amulya
 */

public abstract class Attribute {

    protected static final Logger logger = LogManager.getLogger();
    protected static Game game = Game.getInstance();
    protected static UserThread userThread;//TODO remove?
    protected static Client client = Client.getInstance();
    protected String type;
    protected Orientation orientation;

    //ArrayList<Player> activePlayerList = new ArrayList<>(); // TODO Get from Game

    public static void setUserThread(UserThread userThread) {
        Attribute.userThread = userThread;
    }

    /**
     * All elements on the board must have a performAction method that can be called
     * when the robot find itself in that specific tile.
     * The player that is positioned on the element
     */
    public abstract void performAction(Player player);

    public abstract Node createImage();

    /**
     * Sometimes a robot may find other robot while moving in a given directions.
     * @return true if robot find any robot on its path, false otherwise
     */
    /*
    public boolean checkPlayer(Player player) {
        int x = player.getRobot().getPosition().getX();
        int y = player.getRobot().getPosition().getY();
        Orientation orientation = player.getDirection();

        for(Player targetPlayer: activePlayerList) {
            switch (orientation) {
                case RIGHT:
                    if (targetPlayer.getRobot().getPosition().getX() == x && targetPlayer.getRobot().getPosition().getY() == y + 1)
                        return true;
                    break;
                case LEFT:
                    if (targetPlayer.getRobot().getPosition().getX() == x && targetPlayer.getRobot().getPosition().getY() == y - 1)
                        return true;
                    break;
                case UP:
                    if (targetPlayer.getRobot().getPosition().getX() == x - 1 && targetPlayer.getRobot().getPosition().getY() == y)
                        return true;
                    break;
                case DOWN:
                    if (targetPlayer.getRobot().getPosition().getX() == x + 1 && targetPlayer.getRobot().getPosition().getY() == y)
                        return true;
                    break;
            }
        }
        return false;
    }*/

    /**
     * Sometimes a robot may find hindrance while moving (may encounter wall or antenna)
     * In such case the movement action will be nullified
     */
    public void isValidMove(){

    }

    public String getType() {
        return type;
    }

    public Orientation getOrientation() {
        return orientation;
    }
}