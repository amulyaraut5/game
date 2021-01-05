package game.gameObjects.cards;

import game.Player;
import javafx.scene.image.ImageView;

public class TempUpgradeCard extends UpgradeCard {

    public TempUpgradeCard(String cardName) {
        super(cardName);
    }

    @Override
    public ImageView drawCardImage() {
        return null;
    }

    /**
     * Once you’ve purchased a temporary upgrade,
     * you may keep it in your hand and use it at any time on your turn.
     */
    @Override
    public void handleCard() {
    }

    /**
     * After a temporary upgrade has gone into effect, place it out of play.
     * @return
     */
    public boolean isUsed(){
        return true;
    }

}