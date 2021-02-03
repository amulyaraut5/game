package client.view;

import client.ViewManager;
import client.model.Client;
import javafx.scene.image.ImageView;
import javafx.scene.input.DataFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.enums.CardType;

import java.util.ArrayList;

/**
 * Abstract super class of all view-controller
 *
 * @author simon, sarah
 */
public abstract class Controller {
    protected static final DataFormat cardFormat = new DataFormat("programmingCard");
    private static final Logger logger = LogManager.getLogger();

    private static int positionRegister;
    private static ImageView programmingImageView;
    private static boolean wasFormerRegister = false;

    private static int countSpamCards = 38;
    private static int countTrojanCards = 12;
    private static int countWormCards = 6;
    private static int countVirusCards = 18;

    protected final ViewManager viewManager = ViewManager.getInstance();
    protected final Client client = Client.getInstance();
    protected final String[] robotNames = {"hulkX90", "hammerbot", "smashbot",
            "twonky", "spinbot", "zoombot"};

    public boolean getWasFormerRegister() {
        return wasFormerRegister;
    }

    public void setWasFormerRegister(boolean wasFormerRegister) {
        Controller.wasFormerRegister = wasFormerRegister;
    }

    protected int getCountSpamCards() {
        return countSpamCards;
    }

    public void setCountSpamCards(int countSpamCards) {
        if ((countSpamCards < 0)) this.countSpamCards = 0;
        else this.countSpamCards = countSpamCards;
    }

    public int getCountTrojanCards() {
        return countTrojanCards;
    }

    public void setCountTrojanCards(int countTrojanCards) {
        if ((countTrojanCards < 0)) this.countTrojanCards = 0;
        else this.countTrojanCards = countTrojanCards;
    }

    public int getCountWormCards() {
        return countWormCards;
    }

    public void setCountWormCards(int countWormCards) {
        if ((countWormCards < 0)) this.countWormCards = 0;
        else this.countWormCards = countWormCards;
    }

    public int getCountVirusCards() {
        return countVirusCards;
    }

    public void setCountVirusCards(int countVirusCards) {
        if ((countVirusCards < 0)) this.countVirusCards = 0;
        else this.countVirusCards = countVirusCards;
    }

    public void handleDamageCount(CardType cardType) {
        switch (cardType) {
            case Spam -> setCountSpamCards(getCountSpamCards() - 1);
            case Trojan -> setCountTrojanCards(getCountTrojanCards() - 1);
            case Worm -> setCountWormCards(getCountWormCards() - 1);
            case Virus -> setCountVirusCards(getCountVirusCards() - 1);
        }
    }

    public void handleDamageCount(ArrayList<CardType> cardList) {
        if (cardList.size() == 0) setCountSpamCards(0); //TODO correct?
        else {
            for (CardType cardType : cardList) {
                handleDamageCount(cardType);
            }
        }
    }

    protected CardType generateCardType(String imageDropped) {
        String[] a = imageDropped.split("/");
        String imageName = a[a.length - 1];
        CardType cardName = CardType.valueOf(imageName.substring(0, imageName.length() - 9));
        return cardName;
    }

    public ImageView getProgrammingImageView() {
        return programmingImageView;
    }

    public void setProgrammingImageView(ImageView programmingImageView) {
        Controller.programmingImageView = programmingImageView;
    }

    protected int getPosition() {
        return positionRegister;
    }

    protected void setPosition(int position) {
        positionRegister = position;
    }
}
