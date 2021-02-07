package game.round;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;
import utilities.JSONProtocol.body.TimerStarted;

/**
 * if one player is ready with picking cards, he creates an instance of timer (if timer isn´t running yet)
 */

public class GameTimer extends Thread {
    private static final Logger logger = LogManager.getLogger();

    private final ProgrammingPhase programmingPhase;

    public GameTimer(ProgrammingPhase programmingPhase) {

        this.programmingPhase = programmingPhase;
    }

    /**
     * a method that starts a 30 sec timer
     */
    @Override
    public void run() {
        Server.getInstance().communicateAll(new TimerStarted());
        logger.info("The timer has started");
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        programmingPhase.endProgrammingTimer();
    }
}