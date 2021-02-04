package utilities;

/**
 * A Coordinate saves the 2 dimensional x and y position of gameObjects on the map.
 *
 * @author simon
 */
public class Coordinate {
    private int x, y;

    /**
     * Constructor creates a new Coordinate with given position.
     *
     * @param x horizontal parameter of a position
     * @param y vertical parameter of a position
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Parses a one-dimensional position on the map into a two-dimensional {@link Coordinate}.
     *
     * @param position Position on the map starting from top right to bottom left.
     * @return Coordinate of the given position.
     */
    public static Coordinate parse(int position) {
        int x = position % Constants.MAP_WIDTH;
        int y = position / Constants.MAP_WIDTH;
        return new Coordinate(x, y);
    }

    /**
     * Converts this {@link Coordinate} into a one-dimensional position on the map starting from top right to bottom left.
     *
     * @return position on the map.
     */
    public int toPosition() {
        return x + y * Constants.MAP_WIDTH;
    }

    public void add(Coordinate c) {
        x += c.getX();
        y += c.getY();
    }

    public Coordinate subtract(Coordinate other) {
        return new Coordinate(x - other.x, y - other.y);
    }

    public int getX() {
        return x;
    }

    /**
     * @param x horizontal parameter of a position
     */
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    /**
     * @param y y vertical parameter of a position
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @param obj Coordinate to test
     * @return true if both coordinates are at the same position
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Coordinate) {
            Coordinate c = (Coordinate) obj;
            return (getX() == c.getX()) && (getY() == c.getY());
        } else return false;
    }

    /**
     * Creates a clone of this {@link Coordinate}, which is on the same x and y position.
     * <blockquote>
     * <pre>x.clone() != x</pre>
     * <pre>x.clone().getClass() == x.getClass()</pre>
     * <pre>x.clone().equals(x)</pre>
     * </blockquote>
     *
     * @return a clone of this Coordinate.
     */
    @Override
    public Coordinate clone() {
        return new Coordinate(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public void addToX(int x) {
        this.x = this.x + x;
    }

    public void addToY(int y) {
        this.y = this.y + y;
    }

    public boolean isOutsideMap() {
        return (getX() > Constants.MAP_WIDTH - 1 ||
                getX() < 0) ||
                (getY() > Constants.MAP_HEIGHT - 1 ||
                        getY() < 0);
    }
}
