package utilities;

import javafx.application.Platform;
import javafx.scene.control.Label;
import utilities.JSONProtocol.JSONMessage;

import java.util.Timer;
import java.util.TimerTask;

public interface Updatable {

    /**
     * it displays a text (mostly an error, instruction or information) into a label for a
     * certain amount of time (2,5 sec)
     *
     * @param label the label which should display the information
     * @param text  the text which should get displayed
     */
    static void showInfo(Label label, String text) {
        Platform.runLater(() -> label.setText(text));
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if (label.getText().equals(text)) {
                    Platform.runLater(() -> label.setText(""));
                }
                t.cancel();
            }
        }, 2500);
    }

    void update(JSONMessage message);
}