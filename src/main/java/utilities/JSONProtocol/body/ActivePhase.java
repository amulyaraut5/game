package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class ActivePhase extends JSONBody {
    int phase; //0 => Aufbauphase, 1 => Upgradephase, 2 => Programmierphase, 3 => Aktivierungsphase

    public ActivePhase(int phase) {
        this.phase = phase;
    }

    public int getPhase() {
        return phase;
    }
}
