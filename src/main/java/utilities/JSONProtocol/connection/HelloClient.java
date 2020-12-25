package utilities.JSONProtocol.connection;

import utilities.JSONProtocol.JSONBody;

public class HelloClient extends JSONBody {
    double protocol;

    public HelloClient(double protocol) {

        this.protocol = protocol;
    }

    public double getProtocol() {
        return protocol;
    }
}