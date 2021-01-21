package utilities.enums;

import com.google.gson.annotations.SerializedName;

public enum PhaseState {
    @SerializedName("0") CONSTRUCTION(0),
    @SerializedName("2") PROGRAMMING(2),
    @SerializedName("3") ACTIVATION(3);

    static {
        CONSTRUCTION.next = PROGRAMMING;
        PROGRAMMING.next = ACTIVATION;
        ACTIVATION.next = PROGRAMMING;
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
