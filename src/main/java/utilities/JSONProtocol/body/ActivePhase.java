package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;
import utilities.enums.GameState;

public class ActivePhase extends JSONBody {
    private final GameState phase; //0 => Aufbauphase, 1 => Upgradephase, 2 => Programmierphase, 3 => Aktivierungsphase

    public ActivePhase(GameState phase) {
        this.phase = phase;
    }

    public GameState getPhase() {
        return phase;
    }
}
