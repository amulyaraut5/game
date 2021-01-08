package game.gameObjects.cards;

import game.Game;
import game.Player;
import javafx.scene.image.ImageView;
import utilities.Utilities.CardType;

public abstract class TempUpgradeCard extends UpgradeCard {

    public TempUpgradeCard(CardType card) {
        super(card);
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
    public void handleCard(Game game, Player player) {
        super.handleCard(game, player);
    }

    /**
     * After a temporary upgrade has gone into effect, place it out of play.
     *
     * @return
     */
    public boolean isUsed() {
        return true;
    }

}