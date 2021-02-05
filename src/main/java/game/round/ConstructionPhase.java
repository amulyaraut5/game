package game.round;

import game.Player;
import server.User;
import utilities.Coordinate;
import utilities.JSONProtocol.body.CurrentPlayer;
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.StartingPointTaken;
import utilities.enums.AttributeType;

public class ConstructionPhase extends Phase {

    Player currentPlayer;
    private int currentIndex = 0;

    public ConstructionPhase() {
        currentPlayer = players.get(currentIndex);
        server.communicateAll(new CurrentPlayer(currentPlayer.getID()));
    }

    public void setStartingPoint(User user, int position) {
        Coordinate pos = Coordinate.parse(position);
        Player player = game.userToPlayer(user);

        if (!player.equals(currentPlayer)) {
            player.message(new Error("It's not your move yet!"));
        } else if (pos.isOutsideMap()) {
            player.message(new Error("This is no viable point on the map!"));
        } else if (player.getRobot().getCoordinate() != null) {
            player.message(new Error("You have already set your starting point!"));
        } else if (!map.getTile(pos).hasAttribute(AttributeType.StartPoint)) {
            player.message(new Error("This is no valid StartPoint!"));
        } else if (isStartPointTaken(pos, player)) {
            player.message(new Error("Your chosen position is already taken!"));
        } else {
            //chosen StartingPoint is valid
            player.getRobot().setCoordinate(pos.clone());
            player.getRobot().setStartingPoint(pos.clone());
            server.communicateAll(new StartingPointTaken(player.getID(), position));
            nextPlayer();
        }
    }

    private boolean isStartPointTaken(Coordinate pos, Player player) {
        for (Player other : players) {
            if (!other.equals(player)) {
                Coordinate otherPos = other.getRobot().getCoordinate();
                if (pos.equals(otherPos)) return true;
            }
        }
        return false;
    }

    public void nextPlayer() {
        currentIndex = players.indexOf(currentPlayer);

        if (currentIndex < players.size() - 1) {
            currentPlayer = players.get(++currentIndex);
            server.communicateAll(new CurrentPlayer(currentPlayer.getID()));
        } else {
            game.nextPhase();
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }
}
