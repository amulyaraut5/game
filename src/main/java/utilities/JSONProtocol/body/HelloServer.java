package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class HelloServer extends JSONBody {

    private final double protocol;

    private final String group;

    private final Boolean isAI;

    public HelloServer(double protocol, String group, Boolean isAI) {
        this.protocol = protocol;
        this.group = group;
        this.isAI = isAI;
    }

    public Boolean isAI() {
        return isAI;
    }

    public String getGroup() {
        return group;
    }

    public double getProtocol() {
        return protocol;
    }
}

