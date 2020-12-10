package game.gameObjects.cards;

public class TempUpgradeCard extends UpgradeCard {

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