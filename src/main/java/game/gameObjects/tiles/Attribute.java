package game.gameObjects.tiles;

import client.model.Client;
import game.Game;
import game.Player;
import game.gameActions.Action;
import javafx.scene.canvas.GraphicsContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.UserThread;
import utilities.Coordinate;

/**
 * @author Amulya
 */

public abstract class Attribute {

    protected static final Logger logger = LogManager.getLogger();
    static Game game;
    static UserThread userThread;
    static Client client;
    protected String type;


    public static void setUserThread(UserThread userThread) {
        Attribute.userThread = userThread;
    }

    public static void setClient(Client client) {
        Attribute.client = client;
    }

    public static void setGame(Game game) {
        Attribute.game = game;
    }

    /**
     * All elements on the board must have a performAction method that can be called
     * when the robot find itself in that specific tile.
     * The player that is positioned on the element
     * <p>
     * Another Idea : We can also Robot instead of Player.
     */
    public abstract void performAction(Player player);

    public abstract void draw(GraphicsContext gc, Coordinate position);

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


    @Override
    public String toString() {
        return "Attribute{" +
                "name='" + type + '\'' +
                '}';
    }
}