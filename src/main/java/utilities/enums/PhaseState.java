package utilities.enums;

import com.google.gson.annotations.SerializedName;
import utilities.Utilities;

public enum PhaseState {
    @SerializedName("0") CONSTRUCTION(0),
    @SerializedName("1") UPGRADE(1),
    @SerializedName("2") PROGRAMMING(2),
    @SerializedName("3") ACTIVATION(3);

    static {
        CONSTRUCTION.next = UPGRADE;
        UPGRADE.next = PROGRAMMING;
        PROGRAMMING.next = ACTIVATION;
        ACTIVATION.next = UPGRADE;
    }

    private final int phase;
    private PhaseState next;

    PhaseState(int phase) {
        this.phase = phase;
    }

    public PhaseState getNext() {
        return next;
    }
}
