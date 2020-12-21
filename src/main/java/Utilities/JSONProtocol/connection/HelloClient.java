package Utilities.JSONProtocol.connection;

import Utilities.JSONProtocol.JSONBody;

public class HelloClient extends JSONBody {
    double protocol;

    public HelloClient(double protocol) {

        this.protocol = protocol;
    }

    public double getProtocol() {
        return protocol;
    }
}