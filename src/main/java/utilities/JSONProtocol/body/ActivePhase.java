package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;
import utilities.Utilities.Phase;

public class ActivePhase extends JSONBody {
    Phase phase; //0 => Aufbauphase, 1 => Upgradephase, 2 => Programmierphase, 3 => Aktivierungsphase

    public ActivePhase(Phase phase) {
        this.phase = phase;
    }

    public Phase getPhase() {
        return phase;
    }
}
