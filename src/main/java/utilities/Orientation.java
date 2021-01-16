package utilities;

import com.google.gson.annotations.SerializedName;

public enum Orientation {
    @SerializedName("up") UP,
    @SerializedName("down") DOWN,
    @SerializedName("left") LEFT,
    @SerializedName("right") RIGHT;

    static {
        UP.opposite = DOWN;
        RIGHT.opposite = LEFT;
        DOWN.opposite = UP;
        LEFT.opposite = RIGHT;

        UP.next = RIGHT;
        RIGHT.next = DOWN;
        DOWN.next = LEFT;
        LEFT.next = UP;

        UP.prior = LEFT;
        RIGHT.prior = LEFT;
        DOWN.prior = UP;
        LEFT.prior = RIGHT;
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
