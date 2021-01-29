package client.view;


import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import utilities.JSONProtocol.body.SelectCard;
import utilities.enums.CardType;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 *
 * @author sarah
 */
public class ProgrammingController extends Controller {

    private int interval;
    private Media media;
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    private double widthHBox;
    private double heightHBox;
    private boolean timerEnded = false;

    @FXML
    private HBox hBox1Background;
    @FXML
    private HBox hBox2Background;
    @FXML
    private HBox hBox1;
    @FXML
    private HBox hBox2;
    @FXML
    private AnchorPane timerAnchorPane;
    @FXML
    private Label programInfoLabel;
    @FXML
    private Label timerLabel;


    public void initialize() { //TODO method that gets called when cards were dealt
        widthHBox = hBox1.getPrefWidth() / 5;
        heightHBox = hBox1.getPrefHeight();
        settingsHBoxes(new HBox[]{hBox1, hBox2, hBox1Background, hBox2Background});
        for (int i = 0; i < 5; i++) createBackground(hBox1Background);
        for (int i = 0; i < 4; i++) createBackground(hBox2Background);


    }

    private void settingsHBoxes(HBox [] hBoxes){
        for (HBox hBox : hBoxes) {
            hBox.setSpacing(20);
            hBox.setAlignment(Pos.CENTER_LEFT);
        }
    }
    private void createBackground(HBox hBox){
        ImageView background = new ImageView(new Image(getClass().getResource("/cards/programming/underground-card.png").toString()));
        background.setFitHeight(heightHBox);
        background.setFitWidth(widthHBox - 20);
        hBox.getChildren().add(background);
    }

    public void startProgrammingPhase(ArrayList<CardType> cardList) {
        for (int i = 0 ; i < cardList.size(); i++) {
            StackPane pane = new StackPane();
            pane.setPrefHeight(heightHBox);
            pane.setPrefWidth(widthHBox-20);
            addDropHandling(pane);
            pane.getChildren().add(createImageView(cardList.get(i)));
            if (!(hBox1.getChildren().size() >= 5)) hBox1.getChildren().add(pane);
            else hBox2.getChildren().add(pane);
        }
    }

    private ImageView createImageView(CardType cardName) {
        ImageView programmingCard = new ImageView(new Image(getClass().getResource("/cards/programming/" + cardName + "-card.png").toString()));
        programmingCard.setFitHeight(heightHBox);
        programmingCard.setFitWidth(widthHBox-20);
        programmingCard.setOnDragDetected(e -> {
            Dragboard db = programmingCard.startDragAndDrop(TransferMode.MOVE);
            db.setDragView(programmingCard.snapshot(null, null));
            ClipboardContent cc = new ClipboardContent();
            cc.put(cardFormat, cardName);
            db.setContent(cc);
            setProgrammingImageView(programmingCard);
        });

        return programmingCard;
    }


    protected void addDropHandling(Pane pane) {
        pane.setOnDragOver(e -> {
            Dragboard db = e.getDragboard();
            if (db.hasContent(cardFormat)
                    && getProgrammingImageView() != null
                    && getProgrammingImageView().getParent() != pane
                    && pane.getChildren().isEmpty()) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
        });

        pane.setOnDragExited(e -> {
            Dragboard db = e.getDragboard();
            if (db.hasContent(cardFormat)
                    && getProgrammingImageView()!= null
                    && pane.getChildren().isEmpty()) {
                ((Pane)getProgrammingImageView().getParent()).getChildren().remove(getProgrammingImageView());
                ImageView puffer = getProgrammingImageView();
                puffer.setOnDragDetected(ev->{
                    puffer.setOnDragDetected(event-> {
                        Dragboard dragboard = puffer.startDragAndDrop(TransferMode.MOVE);
                        dragboard.setDragView(puffer.snapshot(null, null));
                        ClipboardContent cc2 = new ClipboardContent();
                        cc2.put(cardFormat, "cardName");
                        dragboard.setContent(cc2);
                        setProgrammingImageView(puffer);
                       });
                });
                pane.getChildren().add(puffer);
            }
        });
        pane.setOnDragDone(e -> {
            client.sendMessage(new SelectCard(null, getPosition()));
        });
    }

    /**
     * by getting protocol TimerStarted, the countdown in the label and the video will start
     * @param allRegistersAsFirst
     */
    public void startTimer(boolean allRegistersAsFirst){
        if(allRegistersAsFirst) programInfoLabel.setText("You were first!");
        else programInfoLabel.setText("Hurry to fill all 5 registers in time!");

        setTimer();
        startVideo();
    }

    private void startVideo(){
        String path = "/video/hourglass-video.mp4";
        media = new Media(getClass().getResource(path).toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView = new MediaView(mediaPlayer);
        mediaView.setFitHeight(200);
        mediaView.setFitWidth(200);
        mediaPlayer.play();
        timerAnchorPane.getChildren().add(mediaView);

    }
    private void setTimer() {
        Timer timer = new Timer();
        interval = 30;
        timerLabel.setVisible(true);
        timer.schedule(new TimerTask() {
            public void run() {
                if(interval > 0 && !timerEnded){
                    Platform.runLater(() -> timerLabel.setText(String.valueOf(interval)));
                    interval--;
                } else
                    timer.cancel();
            }
        }, 1000,1000);
    }

    public void setTimerEnded(boolean timerEnded) {
        this.timerEnded = timerEnded;
    }

    public void reset(){
        hBox2.getChildren().clear();
        hBox1.getChildren().clear();
        timerAnchorPane.getChildren().clear();
        timerLabel.setVisible(false);
        programInfoLabel.setText(" ");
        timerEnded = false;
    }
}


