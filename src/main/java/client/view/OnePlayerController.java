package client.view;

import game.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utilities.JSONProtocol.body.PlayerAdded;



public class OnePlayerController extends Controller{
    @FXML
    private Label nameLabel;
    @FXML
    private ImageView robotIcon;
    @FXML
    private Label infoLabel;

    public Label energyLabel;
    public Label checkBoxLabel;
    private int energy = 5;


    public void setPlayerInformation(Player otherPlayer) {
        nameLabel.setText(otherPlayer.getName());
        String robot = robotNames[otherPlayer.getFigure()];
        robotIcon.setImage(new Image(getClass().getResource("/lobby/" + robot + ".png").toString()));
        energyLabel.setText("Energy " + energy);
    }

    public void setInfoLabel(String text){
        infoLabel.setText(text);
    }

    public void addCheckPoint(int number){
        checkBoxLabel.setText("Checkpoints " + number);
    }

    public void addEnergy(int energyCount){
        energy += energyCount;
        energyLabel.setText("Energy " + energy);
    }
}
