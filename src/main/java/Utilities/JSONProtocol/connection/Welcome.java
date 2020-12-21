package Utilities.JSONProtocol.connection;

import Utilities.JSONProtocol.JSONBody;

public class Welcome extends JSONBody {

    private static int idCounter = 0;
    private int id;

    public Welcome() {
        id = idCounter++;
    }

    public Welcome(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
