package client.view;

import game.Game;
import game.Player;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import utilities.Coordinate;
import utilities.Utilities.Rotation;

public class ActivationController extends Controller{

    private static GameViewController gameViewController;

    @FXML private Label registerNr;
    @FXML private Label activateProgOrBoard;
    @FXML private Label displayResult;
    @FXML private Label  showPriority;
    @FXML private ImageView imageView1;
    @FXML private ImageView imageView2;
    @FXML private ImageView imageView3;


    public void initialize(){
        //registerNr.setText("");
        //changeDirection();
        //moveRobot();
    }


}