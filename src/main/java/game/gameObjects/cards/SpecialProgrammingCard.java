package game.gameObjects.cards;

import javafx.scene.image.ImageView;
import utilities.Utilities.CardType;

public abstract class SpecialProgrammingCard extends ProgrammingCard {

    SpecialProgrammingCard(CardType card) {
        super(card);
    }

    @Override
    public ImageView drawCardImage() {
        return null;
    }

}