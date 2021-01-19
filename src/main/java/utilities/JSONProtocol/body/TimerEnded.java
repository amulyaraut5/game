package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

import java.util.ArrayList;

public class TimerEnded extends JSONBody {
    private ArrayList<Integer> playerIDs;

    public TimerEnded(ArrayList<Integer> playerIDs) {
        this.playerIDs = playerIDs;
    }

    public ArrayList<Integer> getPlayerIDs() {
        return playerIDs;
    }
}
