package client.view;

import game.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import utilities.enums.CardType;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class represents one small playermat of one other player, it contains all necessary information the
 * player needs to know about other players.
 *
 * @author sarah
 */
public class OnePlayerController extends Controller {
    @FXML
    private Label nameLabel;
    @FXML
    private ImageView robotIcon;
    @FXML
    private Label infoLabel;
    @FXML
    private Label infoLabel2;
    @FXML
    private HBox drawDamageHBox;
    @FXML
    private ImageView currentCardImageView;
    @FXML
    private HBox registerHBox;
    @FXML
    private Label energyLabel;
    @FXML
    private Label checkBoxLabel;

    private int energy = 5;

    /**
     * This method gets called automatically by creating the view, fills five registers with backside cards
     * and
     */
    public void initialize() {
        currentCardImageView.setVisible(false);
        fillRegister();
        setHBoxRegisterVisible(false);
    }

    /**
     * This method sets the name and the ocurring profile image and the 5 energy tokens when this other
     * player gets added
     *
     * @param otherPlayer the player which gets added
     */
    public void setPlayerInformation(Player otherPlayer) {
        String playerName = client.getPlayerFromID(otherPlayer.getID()).getName();
        String uniquePlayerName = client.getUniqueName(otherPlayer.getID());
        if(uniquePlayerName.split(" ", 2).length!=1) {
            if(Integer.parseInt(uniquePlayerName.substring(uniquePlayerName.length()-1)) == 1) {
                playerName = client.getPlayerFromID(otherPlayer.getID()).getName();
            }
        }
        nameLabel.setText(playerName);
        String robot = robotNames[otherPlayer.getFigure()];
        robotIcon.setImage(new Image(getClass().getResource("/lobby/" + robot + ".png").toString()));
        addEnergy(0);
        drawDamageHBox.setVisible(false);
    }

    /**
     * This method fills 5 registers in an HBox with the same card image
     */
    private void fillRegister() {
        for (int i = 0; i < 5; i++) {
            ImageView imageView = generateImageView("/cards/programming/underground-card.png", 20, 30);
            registerHBox.getChildren().add(imageView);
        }
    }

    /**
     * This
     *
     * @param card
     */
    public void currentCard(CardType card) {
        infoLabel.setText("Current card ");
        currentCardImageView.setVisible(true);
        currentCardImageView.setImage(new Image(getClass().getResource("/cards/programming/" + card + "-card.png").toString()));
    }

    /**
     * This method sets the text of infoLabel2 and displays it only for a short amount of time
     *
     * @param text which gets added to the label
     */
    public void setInfoLabel2(String text) {
        infoLabel2.setText(text);
        displayingTime(infoLabel2);
    }

    /**
     * This method sets the text of infoLabel
     *
     * @param text which gets added to the label
     */
    public void setInfoLabel(String text) {
        infoLabel.setText(text);
    }

    /**
     * This method sets the text of checkboxLabel to a number
     *
     * @param number of last checkpoint the player reached
     */
    public void addCheckPoint(int number) {
        checkBoxLabel.setText(String.valueOf(number));
    }

    /**
     * The method sets if a player put a card in a register. It only displays which register is already filled, not which
     * card
     *
     * @param registerSelected
     */
    public void cardSelected(int registerSelected) {
        ImageView imageView = (ImageView) registerHBox.getChildren().get(registerSelected - 1);
        imageView.setImage(new Image(getClass().getResource("/cards/programming/backside-card-orange.png").toString()));
    }

    /**
     * This method adds energy and displays this increase in the energyLabel
     *
     * @param energyCount the amount of energy token that get added
     */
    public void addEnergy(int energyCount) {
        energy += energyCount;
        energyLabel.setText(String.valueOf(energy));
    }

    /**
     * This method sets the HBox of the registers visible or invisible
     *
     * @param visible
     */
    public void setHBoxRegisterVisible(boolean visible) {
        registerHBox.setVisible(visible);
    }

    /**
     * This method resets the information related to the phases after one round. It sets everything related to programming
     * phase. For this it fills the registers again and sets the imageView of activationphase not visible
     * the occurring HBox and sets the card
     */
    public void reset() {
        currentCardImageView.setVisible(false);
        registerHBox.getChildren().clear();
        fillRegister();
    }

    /**
     * This method only displays a label for 5 seconds
     *
     * @param node that gets set visible
     */
    public void displayingTime(Node node) {
        Platform.runLater(() -> node.setVisible(true));
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if (node.isVisible()) {
                    Platform.runLater(() -> node.setVisible(false));
                }
                t.cancel();
            }
        }, 5000);
    }

    /**
     * This method displays the images of the amount of damage cards for 5 seconds
     *
     * @param damageCards this contains an array with CardTypes of damage cards
     */
    public void displayDamageCards(ArrayList<CardType> damageCards) {
        drawDamageHBox.getChildren().clear();
        for (CardType damageCard : damageCards) {
            String path = "/cards/programming/" + damageCard.name() + "-card.png";
            ImageView damage = generateImageView(path, 30, 50);
            drawDamageHBox.getChildren().add(damage);
        }
        displayingTime(drawDamageHBox);
    }
}
