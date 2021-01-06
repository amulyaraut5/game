package game.gameObjects.tiles;

import client.model.Client;
import game.Game;
import game.Player;
import javafx.scene.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.UserThread;
import utilities.Utilities;

import javax.print.attribute.standard.JobOriginatingUserName;

/**
 * @author Amulya
 */

public abstract class Attribute {

    protected static final Logger logger = LogManager.getLogger();
    protected static Game game = Game.getInstance();
    protected static UserThread userThread;//TODO remove?
    protected static Client client = Client.getInstance();
    protected String type;
    protected Utilities.Orientation orientation;

    public static void setUserThread(UserThread userThread) {
        Attribute.userThread = userThread;
    }

    /**
     * All elements on the board must have a performAction method that can be called
     * when the robot find itself in that specific tile.
     * The player that is positioned on the element
     * <p>
     * Another Idea : We can also Robot instead of Player.
     */
    public abstract void performAction(Player player);

    public abstract Node createImage();

    /**
     * Sometimes a robot may find another robot while moving in any directions.
     * This can be used to shoot a laser or push oa
     *
     * @return
     */
    public boolean checkPlayer() {
        // TODO - implement Attribute.checkPlayer
        throw new UnsupportedOperationException();
    }

    public String getType() {
        return type;
    }

    public Utilities.Orientation getOrientation() {
        return orientation;
    }
}