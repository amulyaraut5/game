package game.round;

import game.Player;
import server.User;
import utilities.Coordinate;
import utilities.JSONProtocol.body.CurrentPlayer;
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.StartingPointTaken;
import utilities.enums.AttributeType;

import static utilities.Coordinate.parse;

public class ConstructionPhase extends Phase {

    Player currentPlayer = game.getPlayers().get(0);

    public ConstructionPhase() {
        server.communicateAll(new CurrentPlayer(currentPlayer.getID()));
    }

    public void setStartingPoint(User user, int position) {
        Coordinate pos = parse(position);
        Player player = game.userToPlayer(user);

        if (!player.equals(currentPlayer)) {
            player.message(new Error("It's not your move yet!"));
            return;
        }
        if (pos.isOutsideMap()) {
            player.message(new Error("This is no viable point on the map!"));
            return;
        }

        //check if playes has already set their starting point
        if (player.getRobot().getCoordinate() == null) {
            //check if chosen tile is a StartingPoint
            System.out.println(pos);
            boolean isOnStartPoint = game.getMap().getTile(pos).hasAttribute(AttributeType.StartPoint);
            if (isOnStartPoint) {
                //check if no other player is on the chosen tile
                for (Player other : game.getPlayers()) {
                    if (!other.equals(player)) {
                        Coordinate otherPos = other.getRobot().getCoordinate();
                        if (!pos.equals(otherPos)) {
                            //chosen StartingPoint is valid
                            player.getRobot().setCoordinate(pos);
                            server.communicateAll(new StartingPointTaken(player.getID(), position));
                            nextPlayer();
                        } else player.message(new Error("Your chosen position is already taken!"));
                    }
                }
            } else player.message(new Error("This is no valid StartPoint!"));
        } else player.message(new Error("You have already set your starting point!"));

        //check if all players have set their StartingPoint


        for (Player p : players) {
            if (p.getRobot().getCoordinate() == null) return;
        }
    }

    private void nextPlayer() {
        int currentIndex = players.indexOf(currentPlayer);
        if (currentIndex == players.size() - 1) {
            game.nextPhase();
        } else {
            currentPlayer = players.get(currentIndex + 1);
            server.communicateAll(new CurrentPlayer(currentPlayer.getID()));
        }
    }
}
