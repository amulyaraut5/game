package client.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import utilities.JSONProtocol.body.SelectCard;
import utilities.enums.CardType;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class represents the programming phase, where the player chooses programming cards and puts them into
 * his registers. Also the timer gets started by receiving related protocol from the server.
 *
 * @author sarah
 */
public class ProgrammingController extends Controller {
    /**
     * This HBox is behind the HBox that contains the available programming cards and displays default images
     */
    @FXML
    private HBox hBox1Background;
    /**
     * This HBox is behind the HBox that contains the available programming cards and displays default images
     */
    @FXML
    private HBox hBox2Background;
    /**
     * This HBox shows 5 programming cards (if available) which the player can draw into his register.
     */
    @FXML
    private HBox hBox1;
    /**
     * This HBox shows 4 programming cards (if available) which the player can draw into his register.
     */
    @FXML
    private HBox hBox2;
    /**
     * This Pane contains the video of the hourglass
     */
    @FXML
    private AnchorPane timerAnchorPane;
    /**
     * This Label contains information if the timer is running because of the player or that the player has to hurry.
     */
    @FXML
    private Label programInfoLabel;
    /**
     * This Label shows the countdown of the timer
     */
    @FXML
    private Label timerLabel;

    /**
     * Interval means the interval of the timer that is normally 30 sec.
     */
    private int interval;
    /**
     * This is the width of one element of the HBox
     */
    private double widthHBox;
    /**
     * This is the height of one element of the HBox
     */
    private double heightHBox;
    /**
     * This boolean show if the timer has already ended, its default value is false.
     */
    private boolean timerEnded = false;

    /**
     * This automatically called method sets all layout settings of the HBoxes, creates the background
     * and calculates the height and width of the HBoxes' contents.
     */
    public void initialize() {
        widthHBox = hBox1.getPrefWidth() / 5;
        heightHBox = hBox1.getPrefHeight();
        settingsHBoxes(new HBox[]{hBox1, hBox2, hBox1Background, hBox2Background});
        for (int i = 0; i < 5; i++) createBackground(hBox1Background);
        for (int i = 0; i < 4; i++) createBackground(hBox2Background);
    }

    /**
     * This method sets Spacing and Alignment for all hBoxes that occur to choosing programming cards
     *
     * @param hBoxes an Array that contains all hBoxes that should get layout
     */
    private void settingsHBoxes(HBox[] hBoxes) {
        for (HBox hBox : hBoxes) {
            hBox.setSpacing(20);
            hBox.setAlignment(Pos.CENTER_LEFT);
        }
    }

    /**
     * This method creates the background of one HBox with underground card images.
     * The whole HBox now is the default background for its overlapping HBox.
     *
     * @param hBox The HBox that should get images with background cards
     */
    private void createBackground(HBox hBox) {
        ImageView background = generateImageView("/cards/programming/underground-card.png", (int) (widthHBox - 20), (int) heightHBox);
        background.setEffect(new DropShadow(1, Color.BLACK));
        hBox.getChildren().add(background);
    }

    /**
     * This method starts the programming phase by getting the available programming cards and displaying them
     * in two HBoxes and also adds EventHandler to the pane that contains the imageView that also has an EventHandler
     * to allow drag and dropping on the panes that are now children of the HBoxes.
     *
     * @param cardList the list of cards the player can choose from
     */
    public void startProgrammingPhase(ArrayList<CardType> cardList) {
        for (CardType cardType : cardList) {
            StackPane pane = new StackPane();
            pane.setPrefHeight(heightHBox);
            pane.setPrefWidth(widthHBox - 20);
            addDropHandling(pane);
            pane.getChildren().add(createImageView(cardType));
            if (!(hBox1.getChildren().size() >= 5)) hBox1.getChildren().add(pane);
            else hBox2.getChildren().add(pane);
        }
    }

    /**
     * This method creates an Image from a CardType and adds an EventHandler that sets a DragBoard when a Drag gets detected
     * and also the information that this imageView former place wasn't a register.
     *
     * @param cardName The CardType that should get transformed into an image
     * @return the ImageView with EventHandler
     */
    private ImageView createImageView(CardType cardName) {
        ImageView programmingCard = generateImageView("/cards/programming/" + cardName + "-card.png", (int)(widthHBox-20), (int)heightHBox);
        programmingCard.setOnDragDetected(event -> {
            setWasFormerRegister(false);
            setOnDragDetected(programmingCard);
        });
        return programmingCard;
    }

    /**
     * This method checks if dragging the image is allowed on a pane.
     * <p>
     * If this is allowed depends on the facts if the DragBoard has the right DataFormat, if the ImageView that stores
     * the dragged image is not null, that the former parent isn't the pane and that the pane is empty.
     *
     * @param pane the pane that should be the aim for dragging and dropping the imageView
     * @param db the DragBoard of the dragged event
     * @return it returns if dragging the image is allowed
     */
    private boolean checkDragAllowed(Pane pane, Dragboard db) {
        return db.hasContent(cardFormat)
                && getProgrammingImageView() != null
                && getProgrammingImageView().getParent() != pane
                && pane.getChildren().isEmpty();
    }

    /**
     * This method gets called by dragging over a pane. It checks if the drag is allowed.
     *
     * @param e The DragEvent occurs by dragging over the pane
     * @param pane the pane that triggers the event
     */
    private void setOnDragOver(DragEvent e, Pane pane) {
        Dragboard db = e.getDragboard();
        if (checkDragAllowed(pane, db)) {
            e.acceptTransferModes(TransferMode.MOVE);
        }
    }
    /**
     * This method adds the dropped image to the pane and removes it from its former parent.
     * Also it adds again EventHandlers to that ImageView for the next tryout of dragging and dropping.
     *
     * @param e The DragEvent occurs when the drag get exited
     * @param pane the pane that triggers the event
     */
    private void setOnDragExited(DragEvent e, Pane pane) {
        Dragboard db = e.getDragboard();
        if (checkDragAllowed(pane, db)) {
            ((Pane) getProgrammingImageView().getParent()).getChildren().remove(getProgrammingImageView());
            ImageView puffer = getProgrammingImageView();
            puffer.setOnDragDetected(event -> setOnDragDetected(puffer));
            pane.getChildren().add(puffer);
        }
    }

    /**
     * This method adds an EventHandler to an ImageView which creates a DragBoard with DataFormat and sets information
     * about the dragged imageView in the parent class.
     *
     * @param imageView the imageView that gets dragged
     */
    private void setOnDragDetected(ImageView imageView) {
        Dragboard dragboard = imageView.startDragAndDrop(TransferMode.MOVE);
        dragboard.setDragView(imageView.snapshot(null, null));
        ClipboardContent cc2 = new ClipboardContent();
        cc2.put(cardFormat, "cardName");
        dragboard.setContent(cc2);
        setProgrammingImageView(imageView);
    }

    /**
     * This method adds EventHandler to a pane by dragging over it, drag exited and drag done.
     * When a drag is done it gets checked if the former position was a register than
     * the protocol selectCard gets send wil null and the former register position.
     *
     * @param pane the pane that should get EventHandler
     */
    protected void addDropHandling(Pane pane) {
        pane.setOnDragOver(e -> setOnDragOver(e, pane));
        pane.setOnDragExited(e -> setOnDragExited(e, pane));
        pane.setOnDragDone(e -> {
            if (getRegisterPosition() != 0)
                viewClient.sendMessage(new SelectCard(null, getRegisterPosition()));
        });
    }

    /**
     * by getting protocol TimerStarted, the countdown in the label and the video will start.
     *
     * @param allRegistersAsFirst
     */
    public void startTimer(boolean allRegistersAsFirst) {
        if (allRegistersAsFirst) programInfoLabel.setText("You were first!");
        else programInfoLabel.setText("Hurry to fill all 5 registers in time!");

        setTimer();
        startVideo();
    }

    /**
     * This method starts the video with the hourglass and adds it to the timeAnchorPane.
     */
    private void startVideo() {
        String path = "/video/hourglass-video.mp4";
        Media media = new Media(getClass().getResource(path).toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitHeight(200);
        mediaView.setFitWidth(200);
        mediaPlayer.play();
        timerAnchorPane.getChildren().add(mediaView);
    }

    /**
     * This method sets the countdown and counts down from 30.
     */
    private void setTimer() {
        Timer timer = new Timer();
        interval = 30;
        timerLabel.setVisible(true);
        timer.schedule(new TimerTask() {
            public void run() {
                if (interval > 0 && !timerEnded) {
                    Platform.runLater(() -> timerLabel.setText(String.valueOf(interval)));
                    interval--;
                } else
                    timer.cancel();
            }
        }, 1000, 1000);
    }

    /**
     * This method sets the timer ended as soon as the protocol TimerEnded gets received.
     *
     */
    public void setTimerEnded() {
        this.timerEnded = timerEnded;
    }

    /**
     * This method resets all elements and information that get changed during the programming phase.
     */
    public void reset() {
        hBox2.getChildren().clear();
        hBox1.getChildren().clear();
        timerAnchorPane.getChildren().clear();
        timerLabel.setVisible(false);
        programInfoLabel.setText(" ");
        timerEnded = false;
    }
}


