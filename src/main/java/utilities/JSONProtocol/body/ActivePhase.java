package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;
import utilities.enums.PhaseState;

public class ActivePhase extends JSONBody {
    private PhaseState phase; //0 => Aufbauphase, 1 => Upgradephase, 2 => Programmierphase, 3 => Aktivierungsphase

    public ActivePhase(PhaseState phase) {
        this.phase = phase;
    }

    public PhaseState getPhase() {
        return phase;
    }
}
