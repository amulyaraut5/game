package client.view;

import game.Player;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
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
import utilities.ImageHandler;
import utilities.JSONProtocol.body.CardsYouGotNow;
import utilities.JSONProtocol.body.SelectCard;
import utilities.enums.CardType;

/**
 * This class represents the playerMat of the player, it contains the registers where the player has to place
 * programming cards and also information about name, figure, energy, checkpoints,
 * discard deck count and programming deck count.
 *
 * @author sarah, annika
 */
public class PlayerMatController extends Controller {
    /**
     * This HBox is behind the HBox that contains all registers and displays default images.
     */
    @FXML
    private HBox registerHBoxBackground;
    /**
     * This HBox displays the images that get dragged and dropped into its children.
     */
    @FXML
    private HBox registerHBox;
    /**
     * The playerIcon displays the figure of the player.
     */
    @FXML
    private ImageView playerIcon;
    /**
     * This Label shows the name of the player.
     */
    @FXML
    private Label playerMapLabelName;
    /**
     * This HBox will contain Images of the checkPoints the player visited.
     */
    @FXML
    private HBox checkPointsHBox;
    /**
     * This Pane contains everything according to the playerMat.
     */
    @FXML
    private AnchorPane playerMapAnchorPane;
    /**
     * This Label displays the count of the amount of cards in the discard deck.
     */
    @FXML
    private Label discardDeckLabel;
    /**
     * This Label displays the count of the amount of cards in the programming deck.
     */
    @FXML
    private Label programmingDeckLabel;
    /**
     * This Label shows the information that Again card is not allowed in the first register,
     * it gets displayed if someone tries that.
     */
    @FXML
    private Label playerMatInfoLabel;
    /**
     * This HBox shows the energy tokens of the player.
     */
    @FXML
    private HBox energyHBox;
    /**
     * This HBox shows the energy tokens of the player. It represents the second row, if the player gets
     * a lot energy tokens.
     */
    @FXML
    private HBox energyHBox2;
    /**
     *
     */
    private int discardDeckNr = 0;
    private int programmingDeckNr = 20;
    private int playerCards = 20;
    /**
     * The width of the register places.
     */
    private double widthRegisterCard;
    /**
     * The height of the register places.
     */
    private double heightRegisterCard;
    /**
     * This Image stores the Image of one energy cube.
     */
    private Image energyCubeImage;

    /**
     * This Image stores the ImageView that got dropped and gets added to the pane of the register.
     */
    private ImageView droppedImageView;

    /**
     * This stores the position of the former register the card was to check which instruction should get
     * send to server.
     */
    private int positionDroppedCard;

    /**
     * This stores if the player wants to drop an Again Card into the first register.
     */
    private boolean againNotFirst;

    /**
     * This method initializes the layout elements, creates them and adds energy.
     */
    public void initialize() {
        energyCubeImage = new Image(getClass().getResource("/otherElements/energycube.png").toString());
        addEnergy(5);
        playerMatInfoLabel.setVisible(false);
        widthRegisterCard = registerHBox.getPrefWidth() / 5;
        heightRegisterCard = registerHBox.getPrefHeight();
        setSpacingHBoxes();
        createRegisterNumberImages();
        createRegisterBackground();
        createRegisters();
    }

    /**
     * This method gets called when the player gets added. It sets the figure and the name of the player.
     *
     * @param player the player with his id, name etc.
     */
    public void loadPlayerMap(Player player) {
        String name = robotNames[player.getFigure()];
        playerMapLabelName.setText(viewClient.getUniqueName(player.getID()));
        playerIcon.setImage(new Image(getClass().getResource("/lobby/" + name + ".png").toString()));
    }

    /**
     * This method sets spacing to HBoxes according to energy and the registerHBoxes
     */
    private void setSpacingHBoxes() {
        energyHBox.setSpacing(5);
        energyHBox2.setSpacing(5);
        registerHBox.setSpacing(20);
        registerHBoxBackground.setSpacing(20);
    }

    /**
     * This method adds EventHandler to a pane by dragging over it, drag exited and drag done.
     *
     * @param pane the pane that should get EventHandler
     */
    private void addDropHandling(Pane pane) {
        pane.setOnDragOver(e -> setOnDragOver(e, pane));
        pane.setOnDragExited(e -> setOnDragExited(e, pane));
        pane.setOnDragDone(e -> setOnDragDone());
    }

    /**
     * This method gets called by dragging over a pane. It checks if the drag is allowed and looks
     * if the player tries to drop an Again card into the first register.
     *
     * @param e The DragEvent occurs by dragging over the pane
     * @param pane the pane that triggers the event
     */
    private void setOnDragOver(DragEvent e, Pane pane) {
        Dragboard db = e.getDragboard();
        if (checkDragAllowed(pane, db)) {
            positionDroppedCard = registerHBox.getChildren().indexOf(pane);
            positionDroppedCard += 1;
            boolean isFirstRegisterAgain = extractCardType(getProgrammingImageView().getImage().getUrl()).toString().equals("Again");
            boolean isFirstRegister = (positionDroppedCard == 1);
            againNotFirst = !(isFirstRegister && isFirstRegisterAgain);
            if (againNotFirst) e.acceptTransferModes(TransferMode.MOVE);
        }
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
     * This method adds (if it's allowed to drag and not again in the first register)
     * the dropped image to the pane and removes it from its former parent.
     * Also it adds again EventHandlers to that ImageView for the next tryout of dragging and dropping.
     *
     * @param e The DragEvent occurs when the drag get exited
     * @param pane the pane that triggers the event
     */
    private void setOnDragExited(DragEvent e, Pane pane) {
        Dragboard db = e.getDragboard();
        if (positionDroppedCard==1 && !againNotFirst) {
            playerMatInfoLabel.setText("You are not allowed to play Again in first register");
            displayingTime(playerMatInfoLabel);
        }
        else playerMatInfoLabel.setText(" ");
        if (checkDragAllowed(pane, db) && againNotFirst) {
            ((Pane) getProgrammingImageView().getParent()).getChildren().remove(getProgrammingImageView());
            droppedImageView = createImageView(getProgrammingImageView(), positionDroppedCard);
            pane.getChildren().add(droppedImageView);
        }
    }

    /**
     * This method handles the card and send the protocol SelectCard with null and the former position if it was
     * in a register. And additional it checks if the card was a damage card, then it handles the count of the related
     * deck. If everything went correct it sends the protocol SelectCard with the card and the register.
     */
    private void setOnDragDone() {
        CardType cardType = extractCardType(droppedImageView.getImage().getUrl());
        if (getWasFormerRegister() && againNotFirst) viewClient.sendMessage(new SelectCard(null, getRegisterPosition()));
        switch (cardType) {
            case Spam -> viewClient.setCountSpamCards(viewClient.getCountSpamCards() + 1);
            case Virus -> viewClient.setCountVirusCards(viewClient.getCountVirusCards() + 1);
            case Trojan -> viewClient.setCountTrojanCards(viewClient.getCountTrojanCards() + 1);
            case Worm -> viewClient.setCountWormCards(viewClient.getCountWormCards() + 1);
        }
        if(againNotFirst) {
            viewClient.sendMessage(new SelectCard(cardType, positionDroppedCard));
        }
    }

    /**
     * This method adds an EventHandler to an ImageView which creates a DragBoard with DataFormat and sets information
     * about the dragged imageView in the parentclass.
     *
     * @param programmingCardImageView the imageView that should get EventHandler
     * @param position the position if the imageView in the registers
     * @return
     */
    private ImageView createImageView(ImageView programmingCardImageView, int position) {
        programmingCardImageView.setOnDragDetected(event -> {
            Dragboard dragboard = programmingCardImageView.startDragAndDrop(TransferMode.MOVE);
            dragboard.setDragView(programmingCardImageView.snapshot(null, null));
            ClipboardContent cc2 = new ClipboardContent();
            cc2.put(cardFormat, "cardName");
            dragboard.setContent(cc2);
            setRegisterPosition(position);
            setWasFormerRegister(true);
            setProgrammingImageView(programmingCardImageView);
        });

        return programmingCardImageView;
    }

    /**
     * This method resets all elements and information that get changed during the programming phase.
     * It empties the registers and creates them again.
     */
    public void reset() {
        registerHBox.getChildren().clear();
        createRegisters();
        createRegisterBackground();
    }

    /**
     * This method generates an ImageView and adds it to a pane
     *
     * @param path the path of an image that should get added to an ImageView
     * @param pane the Pane which should get a ImageView
     */
    private void addImage(String path, StackPane pane) {
        ImageView imageView = generateImageView(path, (int)(widthRegisterCard - 20), (int) heightRegisterCard);
        pane.getChildren().add(imageView);
    }

    /**
     * This method sets cards if the player didn't fill the registers in time.
     * The images of those cards get added to the registerHBox.
     * @param cardsYouGotNow
     */
    public void setNewCardsYouGotNow(CardsYouGotNow cardsYouGotNow) {
        registerHBox.getChildren().clear();
        for (CardType card : cardsYouGotNow.getCards()) {
            StackPane pane = new StackPane();
            pane.setPrefHeight(heightRegisterCard);
            pane.setPrefWidth(widthRegisterCard - 20);
            addImage("/cards/programming/" + card + "-card.png", pane);
            registerHBox.getChildren().add(pane);
        }
    }

    /**
     * This method fixes the 5 in the programming phase selected programming cards.
     * To achieve this it adds the images of cards to the registerHBoxBackground and removes them from the registerHBox.
     */
    public void fixSelectedCards() {
        if (registerHBox.getChildren().size() > 0) {
            for (int i = registerHBoxBackground.getChildren().size() - 1; i >= 0; i--) {
                registerHBoxBackground.getChildren().set(i, registerHBox.getChildren().get(i));
            }
            registerHBox.getChildren().clear();
        }
    }
    /**
     * This method creates the number information of the 5 registers and adds them to the playerMat.
     */
    private void createRegisterNumberImages() {
        ImageView imageView;
        double positionX = widthRegisterCard * 2 - 20;
        double positionY = heightRegisterCard - 20;
        String path;
        int width = 30;
        int height = 30;
        for (int i = 1; i <= 5; i++) {
            path = "/backgrounds/register/register_" + i + ".png";
            imageView = generateImageView(path, width, height);
            assert imageView != null;
            imageView.setTranslateX(positionX);
            imageView.setTranslateY(positionY);
            positionX = positionX + widthRegisterCard + 3;
            playerMapAnchorPane.getChildren().add(imageView);
        }
    }
    /**
     * This method creates the 5 registers and adds 5 panes with EventHandlers to the registerHBox.
     */
    private void createRegisters() {
        int register = 5;
        for (int i = 0; i < register; i++) {
            StackPane pane = new StackPane();
            pane.setPrefHeight(heightRegisterCard);
            pane.setPrefWidth(widthRegisterCard - 20);
            addDropHandling(pane);
            registerHBox.getChildren().add(pane);
        }
    }

    /**
     * This method creates the 5 background registers and adds 5 panes with default images to the registerHBoxBackground.
     */
    private void createRegisterBackground() {
        registerHBoxBackground.getChildren().clear();
        String path = "/cards/programming/backside-card.png";
        ImageView imageView;
        int width = (int) (widthRegisterCard - 20);
        int height = (int) heightRegisterCard;
        for (int i = 0; i <= 4; i++) {
            imageView = generateImageView(path, width, height);
            registerHBoxBackground.getChildren().add(imageView);
        }
    }

    /**
     * This method adds an image to the playerMat if the player gets a new checkpoint.
     * Depending on the count of checkpoint he has the size of images get changed.
     *
     * @param number the number of the new checkpoint
     */
    public void checkPointReached(int number) {
        int fitHeightWidth;
        String path = "/tiles/controlPoint/controlPoint_" + number + ".png";
        if (checkPointsHBox.getChildren().size() < 4) fitHeightWidth = 30;
        else {
            for (Node node : checkPointsHBox.getChildren()) {
                if (node.getClass().equals(ImageView.class)) {
                    ImageView im = (ImageView) node;
                    im.setFitWidth(20);
                    im.setFitHeight(20);
                }
            }
            fitHeightWidth = 20;
        }
        ImageView imageView = ImageHandler.createImageView(path, fitHeightWidth, fitHeightWidth);
        checkPointsHBox.getChildren().add(imageView);
    }

    /**
     * This method adds an image to the playerMat if the player gets an amount of energy.
     * Depending on the count of energy he has the size of images get changed.
     *
     * @param count the count of new energy
     */
    public void addEnergy(int count) {
        int fitHeightWidth;
        while (count > 0) {
            ImageView energyCube = new ImageView(energyCubeImage);
            if (energyHBox.getChildren().size() > 9) {
                for (Node node : energyHBox.getChildren()) {
                    if (node.getClass().equals(energyCube.getClass())) {
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
            count--;
        }
    }

    /**
     * Sets the number of the Discard Deck.
     * If the amount is 0, the number is reset to 0.
     * Otherwise, the amount is added to the current number.
     *
     * @param amount
     */
    public void setDiscardDeckCounter(int amount) {
        String discardDeck;
        if (amount == 0) {
            discardDeckNr = 0;
            discardDeck = String.valueOf(amount);
        } else {
            discardDeckNr += amount;
            discardDeck = String.valueOf(discardDeckNr);
        }
        discardDeckLabel.setText(discardDeck);
    }

    /**
     * Sets the number of the Programming Deck.
     * If the amount is equal to the playercards, the number is reset.
     * Otherwise, the amount is subtracted from the current number.
     *
     * @param amount
     */
    public void setProgrammingDeckCounter(int amount) {
        String programmingDeck;
        if (amount == playerCards) {
            programmingDeckNr = playerCards;
            programmingDeck = String.valueOf(amount);
        } else {
            programmingDeckNr = programmingDeckNr - amount;
            programmingDeck = String.valueOf(programmingDeckNr);
        }
        programmingDeckLabel.setText(programmingDeck);
    }

    /**
     * Checks if the number of the programming deck is smaller than the amount to be subtracted,
     * if so, the deck is reset and then the amount is subtracted.
     * Otherwise, the amount is subtracted from the deck.
     *
     * @param amount the amount to be subtracted
     */
    public void resetDeckCounter(int amount) {
        if (getProgrammingDeckNr() < amount) {
            setDiscardDeckCounter(0);
            setProgrammingDeckCounter(getPlayerCards());
        }
        setProgrammingDeckCounter(amount);
    }

    /**
     * The playerCards are increased by the amount.
     *
     * @param amount
     */
    public void addPlayerCards(int amount) {
        playerCards += amount;
    }

    /**
     * The playerCards are decreased by the amount.
     *
     * @param amount
     */
    public void subtractPlayerCards(int amount) {
        playerCards -= amount;
    }

    public int getPlayerCards() {
        return playerCards;
    }

    public int getProgrammingDeckNr() {
        return programmingDeckNr;
    }

}




