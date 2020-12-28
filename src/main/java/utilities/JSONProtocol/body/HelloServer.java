package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class HelloServer extends JSONBody {

    private String group;

    private Boolean isAI;

    private double protocol;

    public HelloServer(double protocol, String group, Boolean isAI) {
        this.protocol = protocol;
        this.group = group;
        this.isAI = isAI;
    }

    public String getGroup() {
        return group;
    }

    public Boolean isAI() {
        return isAI;
    }

    public double getProtocol() {
        return protocol;
    }
}

