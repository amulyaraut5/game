package client.view;


import game.Player;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
    public Label programmingDeckLabel;
    public Label playerMatInfoLabel;
    private int programmingDeckNr = 20;
    private int playercards = 20;
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

    private ImageView droppedImageView;
    private int positionDroppedCard;
    private boolean againNotFirst;
    protected void addDropHandling(Pane pane) {
        pane.setOnDragOver(e -> {
            Dragboard db = e.getDragboard();
            if (db.hasContent(cardFormat)
                    && getProgrammingImageView() != null
                    && getProgrammingImageView().getParent() != pane) {
                positionDroppedCard = registerHBox.getChildren().indexOf(pane);
                positionDroppedCard += 1;
                boolean isFirstRegisterAgain = generateCardType(getProgrammingImageView().getImage().getUrl()).toString().equals("Again");
                boolean isFirstRegister = (positionDroppedCard == 1);
                againNotFirst = !(isFirstRegister && isFirstRegisterAgain);
                if (againNotFirst) e.acceptTransferModes(TransferMode.MOVE);
            }
        });

        pane.setOnDragExited(e -> {
            Dragboard db = e.getDragboard();
            if (!againNotFirst) playerMatInfoLabel.setText("You are not allowed to play Again in first register");
            else playerMatInfoLabel.setText(" ");

            if (db.hasContent(cardFormat)
                    && getProgrammingImageView()!= null
                    && againNotFirst) {
                ((Pane)getProgrammingImageView().getParent()).getChildren().remove(getProgrammingImageView());
                droppedImageView = createImageView(getProgrammingImageView(), positionDroppedCard);
                    pane.getChildren().add(droppedImageView);
            }
        });
        pane.setOnDragDone(e -> {
            CardType cardType = generateCardType(droppedImageView.getImage().getUrl());
            client.sendMessage(new SelectCard(cardType, positionDroppedCard));
        });
    }



    private ImageView createImageView(ImageView programmingCardImageView, int position) {
            ImageView imageView = programmingCardImageView;
            imageView.setOnDragDetected(event-> {
                Dragboard dragboard = imageView.startDragAndDrop(TransferMode.MOVE);
                dragboard.setDragView(imageView.snapshot(null, null));
                ClipboardContent cc2 = new ClipboardContent();
                cc2.put(cardFormat, "cardName");
                dragboard.setContent(cc2);
                setPosition(position);
                setProgrammingImageView(imageView);
            });
        return imageView;
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
        pane.getChildren().add(imageView);


    }


    public void setNewCardsYouGotNow(CardsYouGotNow cardsYouGotNow) {
        registerHBox.getChildren().clear();
        for (CardType card : cardsYouGotNow.getCards()) {
            StackPane pane = new StackPane();
            pane.setPrefHeight(heightRegisterCard);
            pane.setPrefWidth(widthRegisterCard-20);
            addImage(new Image(getClass().getResource("/cards/programming/" + card + "-card.png").toString()), pane);
            registerHBox.getChildren().add(pane);
        }
    }

    public void fixSelectedCards(boolean setOn) {
        if(setOn) eventOn = false;
        else eventOn = true;
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
        int fitHeightWidth;
        String controlPoint = String.valueOf(number);
        ImageView imageView = new ImageView(new Image(getClass().getResource("/tiles/controlPoint/controlPoint_" + controlPoint + ".png").toString()));
        if(checkPointsHBox.getChildren().size()<4) fitHeightWidth = 35;
        else{
            for (Node node : checkPointsHBox.getChildren()) {
                if (node.getClass().equals(imageView.getClass())) {
                    ImageView im = (ImageView) node;
                    im.setFitWidth(20);
                    im.setFitHeight(20);
                }
            }
            fitHeightWidth = 20;
        }
        imageView.setFitHeight(fitHeightWidth);
        imageView.setFitWidth(fitHeightWidth);

        checkPointsHBox.getChildren().add(imageView);
    }

    public void addEnergy(int count){
        int fitHeightWidth;
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
                fitHeightWidth = 10;
            } else {
                fitHeightWidth = 15;
            }
            energyCube.setFitHeight(fitHeightWidth);
            energyCube.setFitWidth(fitHeightWidth);
            energyHBox.getChildren().add(energyCube);
            count --;
        }
    }

    public void setDiscardDeckCounter(int amount){
        if(amount == 0){
            discardDeckNr = 0;
            discardDeckLabel.setText(amount + "cards");
        } else {
            discardDeckNr = discardDeckNr + amount;
            discardDeckLabel.setText(discardDeckNr + " cards");
        }
    }

    public void setProgrammingDeckCounter(int amount){
        if(amount == playercards){
            programmingDeckNr = playercards;
            programmingDeckLabel.setText(amount + "cards");
        } else {
            programmingDeckNr = programmingDeckNr - amount;
            programmingDeckLabel.setText(programmingDeckNr + " cards");
        }
    }

    public void addPlayercards (int amount) {
        playercards += amount;
    }

    public void substractPlayerCards (int amount) {
        playercards -= amount;
    }

    public int getPlayercards() {
        return playercards;
    }

    public int getDiscardDeckNr() {
        return discardDeckNr;
    }

    public int getProgrammingDeckNr() {
        return programmingDeckNr;
    }
}




