package client.view;

import game.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import utilities.enums.CardType;

import java.util.ArrayList;

/**
 * This class represents one small playerMat of one other player, it contains all necessary information the
 * player needs to know about other players.
 *
 * @author sarah, annika
 */
public class OnePlayerController extends Controller {
    /**
     * This Label displays the name of the other player
     */
    @FXML
    private Label nameLabel;
    /**
     * This ImageView displays the figure of the other player
     */
    @FXML
    private ImageView robotIcon;
    /**
     * This Label displays information about if the player is current player, etc.. It is placed above the
     * little playerMat
     */
    @FXML
    private Label infoLabel;
    /**
     * This Label displays information that are only displayed for a short time,
     * e.g. if the player got damage
     */
    @FXML
    private Label displayingLabel;
    /**
     *
     */
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
     * This method sets the name and the occurring profile image and the 5 energy tokens when this other
     * player gets added.
     *
     * @param otherPlayer the player which gets added
     */
    public void setPlayerInformation(Player otherPlayer) {
        String playerName = viewClient.getUniqueName(otherPlayer.getID());

        nameLabel.setText(playerName);
        String robot = robotNames[otherPlayer.getFigure()];
        robotIcon.setImage(new Image(getClass().getResource("/lobby/" + robot + ".png").toString()));
        addEnergy(0);
        drawDamageHBox.setVisible(false);
    }

    /**
     * This method fills 5 registers in an HBox with the same card image.
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
     * @param card //TODO
     */
    public void currentCard(CardType card) {
        infoLabel.setText("Current card ");
        currentCardImageView.setVisible(true);
        currentCardImageView.setImage(new Image(getClass().getResource("/cards/programming/" + card + "-card.png").toString()));
    }

    /**
     * This method sets the text of infoLabel2 and displays it only for a short amount of time.
     *
     * @param text which gets added to the label
     */
    public void setDisplayingLabel(String text) {
        displayingLabel.setText(text);
        displayingTime(displayingLabel);
    }

    /**
     * This method sets the text of infoLabel.
     *
     * @param text which gets added to the label
     */
    public void setInfoLabel(String text) {
        infoLabel.setText(text);
    }

    /**
     * This method sets the text of checkboxLabel to a number.
     *
     * @param number of last checkpoint the player reached
     */
    public void addCheckPoint(int number) {
        checkBoxLabel.setText(String.valueOf(number));
    }

    /**
     * The method sets if a player put a card in a register. It only displays which register is already filled, not which
     * card.
     *
     * @param registerSelected //TODO
     */
    public void cardSelected(int registerSelected) {
        ImageView imageView = (ImageView) registerHBox.getChildren().get(registerSelected - 1);
        imageView.setImage(new Image(getClass().getResource("/cards/programming/backside-card-orange.png").toString()));
    }

    /**
     * This method adds energy and displays this increase in the energyLabel.
     *
     * @param energyCount the amount of energy token that get added
     */
    public void addEnergy(int energyCount) {
        energy += energyCount;
        energyLabel.setText(String.valueOf(energy));
    }

    /**
     * This method sets the HBox of the registers visible or invisible.
     *
     * @param visible //TODO
     */
    public void setHBoxRegisterVisible(boolean visible) {
        registerHBox.setVisible(visible);
    }

    /**
     * This method resets the information related to the phases after one round. It sets everything related to programming
     * phase. For this it fills the registers again and sets the imageView of activationPhase not visible
     * the occurring HBox and sets the card.
     */
    public void reset() {
        currentCardImageView.setVisible(false);
        registerHBox.getChildren().clear();
        fillRegister();
    }



    /**
     * This method displays the images of the amount of damage cards for 5 seconds.
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
