package game.gameObjects.tiles;

import game.Player;

public abstract class Attribute {

	public String name;

	public abstract void performAction(Player player);

	public boolean checkPlayer() {
		// TODO - implement Attribute.checkPlayer
		throw new UnsupportedOperationException();
	}

}