package client.view;


import game.Player;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import utilities.JSONProtocol.body.CardSelected;
import utilities.JSONProtocol.body.CardsYouGotNow;
import utilities.JSONProtocol.body.SelectCard;
import utilities.enums.CardType;

/**
 * @author sarah
 */
public class PlayerMatController extends Controller{

    @FXML
    private HBox registerHBoxBackground;
    @FXML
    private HBox registerHBox;
    @FXML
    private ImageView playerIcon;
    @FXML
    private Label playerMapLabelName;
    @FXML
    private HBox checkPointsHBox;
    @FXML
    private AnchorPane playerMapAnchorPane;
    @FXML
    private Label discardDeckLabel; //TODO ?
    private int discardDeckNr = 0;
    @FXML
    private Label programmingDeckLabel;
    public Label playerMatInfoLabel;
    private int programmingDeckNr = 20;
    @FXML
    private HBox energyHBox;
    public HBox energyHBox2;

    private boolean eventOn = true;
    private double widthRegisterCard;
    private double heightRegisterCard;



    private Image energyCubeImage;
    public void initialize() {

        energyCubeImage = new Image(getClass().getResource("/otherElements/energycube.png").toString());
        energyHBox.setSpacing(5);
        energyHBox2.setSpacing(5);
        addEnergy(5);
        widthRegisterCard = registerHBox.getPrefWidth() / 5;
        heightRegisterCard = registerHBox.getPrefHeight();
        registerHBox.setSpacing(20);
        registerHBoxBackground.setSpacing(20);
        createRegisterNumberImages();
        createRegisterBackground();
        createRegisters();

        //addDropHandling(registerHBox);

        //eventOn = true;
    }
    public void reset(){
        registerHBox.getChildren().clear();
        eventOn = true;
        createRegisters();
    }


    private void createRegisters(){
        int register = 5;
        for(int i = 0; i< register; i++) {
            StackPane pane = new StackPane();
            pane.setPrefHeight(heightRegisterCard);
            pane.setPrefWidth(widthRegisterCard-20);
            pane.setStyle("-fx-border-color: blue;");
            addDropHandling(pane);
            registerHBox.getChildren().add(pane);
        }
    }

    public void loadPlayerMap(Player player) {
        String name = robotNames[player.getFigure()];
        playerIcon.setImage(new Image(getClass().getResource("/lobby/" + name + ".png").toString()));
        playerMapLabelName.setText(player.getName() + " " + player.getID());
    }


    private void addImage(Image i, StackPane pane) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(widthRegisterCard - 20);
        imageView.setFitHeight(heightRegisterCard);
        imageView.setImage(i);
        imageView.setOnDragDetected(mouseEvent -> setOnDragDetected(mouseEvent, imageView, pane));
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
            String cardName = getImageDropped();
            int registerNumber = registerHBox.getChildren().indexOf(pane) + 1;
            client.sendMessage(new SelectCard(CardType.valueOf(cardName), registerNumber));

        }
        event.setDropCompleted(true);
        event.consume();


    }

    public void setNewCardsYouGotNow(CardsYouGotNow cardsYouGotNow) {
        registerHBox.getChildren().clear();
        for (CardType card : cardsYouGotNow.getCards()) {
            StackPane pane = createNewPane(false);
            addImage(new Image(getClass().getResource("/cards/programming/" + card + "-card.png").toString()), pane);
            registerHBox.getChildren().add(pane);
        }


    }

    public void fixSelectedCards(boolean setOn) {
        if(setOn) eventOn = false;
        else eventOn = true;
    }

    private void mouseDragOver(DragEvent event, StackPane pane) {

        pane.setStyle("-fx-border-color: #ff0000;"
                + "-fx-border-width: 5;"
                + "-fx-background-color: #C6C6C6;"
                + "-fx-border-style: solid;");
        event.acceptTransferModes(TransferMode.ANY);
        event.consume();


    }

    private void createRegisterNumberImages() {
        double positionX = widthRegisterCard * 2 - 20;
        double positionY = heightRegisterCard - 20;
        for (int i = 1; i <= 5; i++) {
            ImageView imageView = new ImageView(new Image(getClass().getResource("/backgrounds/register/register_" + i + ".png").toString()));
            imageView.setFitHeight(30);
            imageView.setFitWidth(30);
            imageView.setTranslateX(positionX);
            imageView.setTranslateY(positionY);
            positionX += widthRegisterCard;
            positionX += 3;
            playerMapAnchorPane.getChildren().add(imageView);
        }

    }

    private void createRegisterBackground() {
        for (int i = 0; i <= 4; i++) {
            ImageView background = new ImageView(new Image(getClass().getResource("/cards/programming/backside-card.png").toString()));
            background.setFitHeight(heightRegisterCard);
            background.setFitWidth(widthRegisterCard - 20);
            background.setDisable(true);
            registerHBoxBackground.getChildren().add(background);
        }
    }

    public void checkPointReached(int number){
        String controlPoint = String.valueOf(number);
        ImageView imageView = new ImageView(new Image(getClass().getResource("/tiles/controlPoint/controlPoint_" + controlPoint + ".png").toString()));
        if(checkPointsHBox.getChildren().size()<4){
            imageView.setFitHeight(35);
            imageView.setFitWidth(35);
        } else {
            for (Node node : checkPointsHBox.getChildren()){
                if(node.getClass().equals(imageView.getClass())){
                    ImageView im = (ImageView) node;
                    im.setFitWidth(20);
                    im.setFitHeight(20);

                }
            }
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
        }

        checkPointsHBox.getChildren().add(imageView);
    }

    public void addEnergy(int count){
        while(count>0){
            ImageView energyCube = new ImageView(energyCubeImage);
            if(energyHBox.getChildren().size()>9){
                for (Node node : energyHBox.getChildren()){
                    if(node.getClass().equals(energyCube.getClass())){
                        ImageView im = (ImageView) node;
                        im.setFitWidth(10);
                        im.setFitHeight(10);
                    }
                }
                energyCube.setFitHeight(10);
                energyCube.setFitWidth(10);
                energyHBox2.getChildren().add(energyCube);
            } else {
                energyCube.setFitHeight(15);
                energyCube.setFitWidth(15);
                energyHBox.getChildren().add(energyCube);
            }

            count --;
        }

    }


    public void setDiscardDeckCounter(int amount){
        if(amount == 0){
            discardDeckLabel.setText(amount + "cards");
        } else {
            discardDeckNr = discardDeckNr + amount;
            discardDeckLabel.setText(discardDeckNr + " cards");
        }
    }

    public void setProgrammingDeckCounter(int amount){
        if(amount == 20){
            programmingDeckLabel.setText(amount + "cards");
        } else {
            programmingDeckNr = programmingDeckNr - amount;
            programmingDeckLabel.setText(programmingDeckNr + " cards");
        }
    }
}




