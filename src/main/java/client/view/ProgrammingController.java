package client.view;


import javafx.application.Platform;

import javafx.fxml.FXML;
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
        hBox1.setSpacing(20);
        hBox2.setSpacing(20);
        hBox1Background.setSpacing(20);
        hBox2Background.setSpacing(20);

        for (int i = 0; i < 5; i++) createBackground(hBox1Background);
        for (int i = 0; i < 4; i++) createBackground(hBox2Background);


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


    private void setOnDragDetected(MouseEvent mouseEvent, ImageView imageView) {

        Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putImage(imageView.getImage());
        setImageDropped(imageView.getImage().getUrl());
        db.setContent(content);
        imageView.setImage(null);
        mouseEvent.consume();

    }


    private void addImage(Image i, StackPane pane) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(widthHBox - 20);
        imageView.setFitHeight(heightHBox);
        imageView.setImage(i);
        imageView.setOnDragDetected(mouseEvent -> setOnDragDetected(mouseEvent, imageView));

        pane.getChildren().add(imageView);


    }

    private void mouseDragDropped(DragEvent event, StackPane pane) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasImage()) {
            success = true;
            if (!pane.getChildren().isEmpty()) {
                pane.getChildren().remove(0);
            }

            Image img = db.getImage();
            addImage(img, pane);


        }
        event.setDropCompleted(success);
        event.consume();
    }

    private void mouseDragOver(DragEvent event, StackPane pane) {

        pane.setStyle("-fx-border-color: red;"
                + "-fx-border-width: 5;"
                + "-fx-background-color: #C6C6C6;"
                + "-fx-border-style: solid;");
        event.acceptTransferModes(TransferMode.ANY);
        event.consume();
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


