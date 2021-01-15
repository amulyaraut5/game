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
        changeDirection();
        moveRobot();
    }
    // <----------------------Only For Test---------------------------->
    public static void setGameViewController(GameViewController gameViewController){
        ActivationController.gameViewController = gameViewController;

    }
    public void changeDirection(){

        ImageView robotImageView= (ImageView) gameViewController.getFields()[7][8].getChildren().get(gameViewController.getFields()[7][8].getChildren().size()-1);
        double currentDirection = robotImageView.rotateProperty().getValue();
        robotImageView.rotateProperty().setValue(currentDirection - 90);
    }
    // Called twice will move the tile image
    public void moveRobot(){
        ImageView robotImageView = (ImageView) gameViewController.getFields()[7][8].getChildren().get(gameViewController.getFields()[7][8].getChildren().size()-1);
        gameViewController.getFields()[7][8].getChildren().remove(gameViewController.getFields()[7][8].getChildren().size()-1);
        gameViewController.getFields()[7][6].getChildren().add(robotImageView);

        /*TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(2));
        transition.setToX(35);
        transition.setToY(45);
        transition.setNode(robotImageView);
        transition.play();

         */




    }
    // <----------------------Only For Test---------------------------->

    public void handleMovement(int playerId, int to){
        // TODO Handle Player differently???
        /*
        for(Player player: Game.getInstance().getPlayerList()) {
            if (player.getPlayerID() == playerId) {
                // Get the Robot position from the Board
                Coordinate oldRobotPosition = player.getRobot().getPosition();
                int x = oldRobotPosition.getX();
                int y = oldRobotPosition.getY();
                //int position = convertToInt(Coordinate coordinate);

                //Coordinate newRobotPosition = reconvertToCoordinate(to);
                // int newX = newRobotPosition.getX();
                // int newY = newRobotPosition.getY();

                // Get ImageView from the old position
                ImageView imageView = (ImageView) gameViewController.getFields()[x][y].getChildren().get(gameViewController.getFields()[x][y].getChildren().size()-1);
                // Remove the imageView from the old position
                gameViewController.getFields()[x][y].getChildren().remove(gameViewController.getFields()[x][y].getChildren().size()-1);
                // Set the imageView to new position
                // Change x and y to newX and newY
                gameViewController.getFields()[x][y].getChildren().add(imageView);
            }
        }

         */
    }
    public void handlePlayerTurning(int playerID,Rotation rotation) {
        // TODO Handle Player differently???
        /*
        for(Player player: Game.getInstance().getPlayerList()){
            if(player.getPlayerID() == playerID){

                Coordinate oldRobotPosition = player.getRobot().getPosition();
                int x = oldRobotPosition.getX();
                int y = oldRobotPosition.getY();
                //int position = convertToInt(Coordinate coordinate);

                // Get the imageView from that position
                ImageView robotImageView = (ImageView) gameViewController.getFields()[x][y].getChildren().get(gameViewController.getFields()[x][y].getChildren().size()-1);
                // Direction of robotImageView
                double currentDirection = robotImageView.rotateProperty().getValue();

                //Turn the robot based on the direction
                if (rotation.equals(Rotation.LEFT)) {
                    robotImageView.rotateProperty().setValue(currentDirection - 90);
                } else {
                    robotImageView.rotateProperty().setValue(currentDirection + 90);
                }
            }
        }

         */
    }


}