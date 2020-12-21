package Utilities.JSONProtocol.connection;

import Utilities.JSONProtocol.JSONBody;

public class Welcome extends JSONBody {

    private int id;
    public Welcome(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
