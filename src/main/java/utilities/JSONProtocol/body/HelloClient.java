package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class HelloClient extends JSONBody {
    private double protocol;

    public HelloClient(double protocol) {
        this.protocol = protocol;
    }

    public double getProtocol() {
        return protocol;
    }
}