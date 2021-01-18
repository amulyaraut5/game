package game.gameObjects.cards.damage;

import game.Game;
import game.Player;
import game.gameObjects.cards.Card;
import game.gameObjects.cards.DamageCard;
import javafx.scene.image.ImageView;
import utilities.ImageHandler;
import utilities.enums.CardType;

import java.util.ArrayList;

/**
 * @author annika
 */
public class Virus extends DamageCard {

    public Virus() {
        card = CardType.Virus;
    }

    /**
     * Any robot on the board within a six-space radius must immediately draw a virus card.
     *
     * @param game
     * @param player
     */
    @Override
    public void handleCard(Game game, Player player) {

        int robotX = player.getRobot().getPosition().getX();
        int robotY = player.getRobot().getPosition().getY();
        ArrayList<Player> allPlayers = game.getPlayers();

        for (Player otherPlayer : allPlayers) {
            int otherRobotX = otherPlayer.getRobot().getPosition().getX();
            int otherRobotY = otherPlayer.getRobot().getPosition().getY();

            if (otherPlayer != player && (otherRobotX <= robotX + 6 || otherRobotY <= robotY + 6)) {
                Card virusCard = game.getVirusDeck().pop();
                otherPlayer.getDiscardedProgrammingDeck().getDeck().add(virusCard);
            }
        }
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/cards/programming/virus-card.png");
    }

}
