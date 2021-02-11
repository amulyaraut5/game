package client.model;

import client.ViewManager;
import game.Player;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Constants;
import utilities.JSONProtocol.JSONBody;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.Multiplex;
import utilities.JSONProtocol.body.Error;
import utilities.Updatable;
import utilities.enums.CardType;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public abstract class Client {

    protected static final Logger logger = LogManager.getLogger();

    private static int countSpamCards = Constants.SPAM_CARDCOUNT;
    private static int countTrojanCards = Constants.TROJAN_CARDCOUNT;
    private static int countWormCards = Constants.WORM_CARDCOUNT;
    private static int countVirusCards = Constants.VIRUS_CARDCOUNT;

    protected final ArrayList<Player> players = new ArrayList<>();
    protected final ArrayList<Player> rebootingAIs = new ArrayList<>();

    protected int thisPlayersID;

    private Socket socket;
    private ReaderThread readerThread;
    private int port = Constants.PORT;
    private PrintWriter writer;

    /**
     * this method reduces the number of damage cards on the associated deck.
     *
     * @param cardList received damage cards
     */
    public void handleDamageCount(ArrayList<CardType> cardList) {
        for (CardType cardType : cardList) {
            switch (cardType) {
                case Spam -> setCountSpamCards(countSpamCards - 1);
                case Trojan -> setCountTrojanCards(countTrojanCards - 1);
                case Worm -> setCountWormCards(countWormCards - 1);
                case Virus -> setCountVirusCards(countVirusCards - 1);
            }
        }
    }

    protected abstract void handleMessage(JSONMessage message);

    /**
     * This method establishes the connection between the server and the client using the assigned hostname and port.
     * If this was successful it creates a ReaderThread and a WriterThread which handle the communication onwards.
     */
    public boolean establishConnection() {
        try {
            logger.info("Trying to connect to port " + port + ".");
            String hostname = "localhost";
            socket = new Socket(hostname, port);
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

            readerThread = new ReaderThread(socket, this);
            readerThread.start();

            logger.info("Connection to server successful.");
            return true;
        } catch (IOException e) {
            logger.warn("No connection to server: " + e.getMessage());
        }
        return false;
    }

    /**
     * Sends a JSONMessage to the server. The JSONMessage is serialized therefore.
     *
     * @param jsonBody Body of the JSONMessage which should be sent.
     */
    public void sendMessage(JSONBody jsonBody) {
        String json = Multiplex.serialize(JSONMessage.build(jsonBody));
        logger.debug("Protocol sent: " + json);
        writer.println(json);
        writer.flush();
    }

    /**
     * This method ends the client program. The reader and writer threads get interrupted and the socket is closed.
     */
    public void disconnect() {
        disconnect(null);
    }

    /**
     * This method should be called if a critical exception occurs in the client. (e.g. socket closed)
     * The reader and writer threads get interrupted and the socket is closed.
     *
     * @param ex The exception which occurred
     */
    public void disconnect(Exception ex) {
        readerThread.interrupt();
        if (ex != null) logger.warn("The server is no longer reachable: " + ex.getMessage());
        try {
            socket.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        logger.info("The connection with the server is closed.");
        if (this instanceof ViewClient) {
            Platform.runLater(() -> ViewManager.getInstance().resetAll());
            Updatable controller = ((ViewClient) this).getCurrentController();
            controller.update(JSONMessage.build(new Error("Server no longer reachable!")));
        }
    }

    /**
     * Gets a player based on their ID from the list of players saved in {@link ViewClient}.
     *
     * @param id ID of the wanted player.
     * @return Unique player with the ID, {@code null} if no player with the ID exists.
     */
    public Player getPlayerFromID(int id) {
        for (Player player : players) {
            if (player.getID() == id) return player;
        }
        return null;
    }

    /**
     * Obtains a unique name of the player.
     * <p>
     * If {@code player.getName()} is not unique, the id is appended to the name.
     * <p>
     * e.g. '{@code Alice}' or '{@code Alice #3}' could be the returned name.
     *
     * @param id ID of the player from whom a unique name should be obtained.
     * @return unique name of the player.
     */
    public String getUniqueName(int id) {
        Player player = getPlayerFromID(id);
        for (Player p : players) {
            if (player != p && player.getName().equals(p.getName())){
                String uniqueName = player.getName() + " #" + player.getID();
                return uniqueName;
            }
        }
        return player.getName();
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getCountSpamCards() {
        return countSpamCards;
    }

    public void setCountSpamCards(int countSpamCards) {
        Client.countSpamCards = Math.max(countSpamCards, 0);
    }

    public int getCountTrojanCards() {
        return countTrojanCards;
    }

    public void setCountTrojanCards(int countTrojanCards) {
        Client.countTrojanCards = Math.max(countTrojanCards, 0);
    }

    public int getCountWormCards() {
        return countWormCards;
    }

    public void setCountWormCards(int countWormCards) {
        Client.countWormCards = Math.max(countWormCards, 0);
    }

    public int getCountVirusCards() {
        return countVirusCards;
    }

    public void setCountVirusCards(int countVirusCards) {
        Client.countVirusCards = Math.max(countVirusCards, 0);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getThisPlayersID() {
        return thisPlayersID;
    }
}
