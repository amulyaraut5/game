package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class SetStartingPoint extends JSONBody {
    int position;

    public SetStartingPoint(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
