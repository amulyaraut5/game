package Utilities.JSONProtocol.Body;

public class GroupIdentification {
    double protocol;
    String group;
    boolean isAI;

    public GroupIdentification(double protocol, String group, boolean isAI) {
        this.protocol = protocol;
        this.group = group;
        this.isAI = isAI;
    }

    public double getProtocol() {
        return protocol;
    }

    public void setProtocol(double protocol) {
        this.protocol = protocol;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isAI() {
        return isAI;
    }

    public void setAI(boolean AI) {
        isAI = AI;
    }
}
