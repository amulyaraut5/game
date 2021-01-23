package client.view;

import game.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import utilities.JSONProtocol.body.CardSelected;
import utilities.JSONProtocol.body.PlayerAdded;



public class OnePlayerController extends Controller{
    @FXML
    private Label nameLabel;
    @FXML
    private ImageView robotIcon;
    @FXML
    private Label infoLabel;

    public HBox registerHBox;
    public Label energyLabel;
    public Label checkBoxLabel;
    private int energy = 5;


    public void setPlayerInformation(Player otherPlayer) {
        nameLabel.setText(otherPlayer.getName());
        String robot = robotNames[otherPlayer.getFigure()];
        robotIcon.setImage(new Image(getClass().getResource("/lobby/" + robot + ".png").toString()));
        addEnergy(0);
    }
    public void initialize(){
        for(int i = 0; i<5; i++){
            ImageView imageView = new ImageView(new Image(getClass().getResource("/cards/programming/underground-card.png").toString()));
            imageView.setFitWidth(20);
            imageView.setFitHeight(30);
            registerHBox.getChildren().add(imageView);
        }
        setHBoxRegisterVisible(false);

    }
    public void setInfoLabel(String text){
        infoLabel.setText(text);
    }

    public void addCheckPoint(int number){
        checkBoxLabel.setText( String.valueOf(number));
    }

    public void cardSelected(int registerSelected){
       ImageView imageView = (ImageView) registerHBox.getChildren().get(registerSelected-1);
       imageView.setImage(new Image(getClass().getResource("/cards/programming/backside-card-orange.png").toString()));

    }
    public void addEnergy(int energyCount){
        energy += energyCount;
        energyLabel.setText(String.valueOf(energy));
    }

    public void setHBoxRegisterVisible(boolean visible) {
        registerHBox.setVisible(visible);
    }
}
