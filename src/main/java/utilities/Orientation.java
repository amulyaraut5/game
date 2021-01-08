package utilities;

import com.google.gson.annotations.SerializedName;

public enum Orientation {
    @SerializedName("up") UP,
    @SerializedName("down") DOWN,
    @SerializedName("left") LEFT,
    @SerializedName("right") RIGHT;

    static {
        Orientation.UP.opposite = Orientation.DOWN;
        Orientation.RIGHT.opposite = Orientation.LEFT;
        Orientation.DOWN.opposite = Orientation.UP;
        Orientation.LEFT.opposite = Orientation.RIGHT;

        Orientation.UP.next = Orientation.RIGHT;
        Orientation.RIGHT.next = Orientation.DOWN;
        Orientation.DOWN.next = Orientation.LEFT;
        Orientation.LEFT.next = Orientation.UP;

        Orientation.UP.prior = Orientation.LEFT;
        Orientation.RIGHT.prior = Orientation.LEFT;
        Orientation.DOWN.prior = Orientation.UP;
        Orientation.LEFT.prior = Orientation.RIGHT;
    }

    private Orientation opposite;
    private Orientation next;
    private Orientation prior;

    public Orientation getNext() {
        return next;
    }

    public Orientation getPrevious() {
        return prior;
    }

    public Orientation getOpposite() {
        return opposite;
    }

    public Coordinate toVector() {
        switch (this) {
            case UP -> {
                return new Coordinate(0, -1);
            }
            case RIGHT -> {
                return new Coordinate(1, 0);
            }
            case DOWN -> {
                return new Coordinate(0, 1);
            }
            case LEFT -> {
                return new Coordinate(-1, 0);
            }
            default -> throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}
