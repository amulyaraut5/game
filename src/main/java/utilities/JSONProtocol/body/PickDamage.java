package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class PickDamage extends JSONBody {
    int count;

    public PickDamage(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
