package game;

/**
 * TODO
 */
@Deprecated
public class GameController{

	private GameController instance;

	public GameController getInstance() {
		if (instance == null) {
			instance = new GameController();
		}
		return instance;
	}
}