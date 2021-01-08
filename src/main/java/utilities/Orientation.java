package utilities;

import com.google.gson.annotations.SerializedName;

public enum Orientation {
    @SerializedName("up") UP,
    @SerializedName("down") DOWN,
    @SerializedName("left") LEFT,
    @SerializedName("right") RIGHT;

    static {
        Orientation.UP.oppositeOrientation = Orientation.DOWN;
        Orientation.DOWN.oppositeOrientation = Orientation.UP;
        Orientation.LEFT.oppositeOrientation = Orientation.RIGHT;
        Orientation.RIGHT.oppositeOrientation = Orientation.LEFT;
    }

    private Orientation oppositeOrientation;

    public Orientation getOpposite() {
        return oppositeOrientation;
    }
}