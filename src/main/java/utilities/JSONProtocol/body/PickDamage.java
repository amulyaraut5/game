package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class PickDamage extends JSONBody {
    private final int count;

    public PickDamage(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
