package Utilities.JSONProtocol.connection;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class for the Identifier in the Body
 */
public class ID {

    private int id;

    public int getId() {
        return id;
    }

    //TODO generate random ID
    public void setId() {
        int id = ThreadLocalRandom.current().nextInt(100, 900);
        this.id = id;
    }

}
