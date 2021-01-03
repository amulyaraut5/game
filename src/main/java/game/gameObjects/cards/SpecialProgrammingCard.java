package game.gameObjects.cards;

import javafx.scene.image.ImageView;

public class SpecialProgrammingCard extends ProgrammingCard {

    SpecialProgrammingCard(String name) {
        super(name);
    }

    @Override
    public ImageView drawCardImage() {
        return null;
    }

}