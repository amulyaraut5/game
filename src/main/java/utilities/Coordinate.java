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

    public void add(Coordinate c) {
        x += c.getX();
        y += c.getY();
    }

    public Coordinate subtract(Coordinate another) {
        return new Coordinate(this.x - another.x, this.y - another.y);
    }

    /**
     * Methods updates the position of a coordinate to given attributes.
     *
     * @param x horizontal parameter of a position
     * @param y vertical parameter of a position
     */
    public void update(int x, int y) {
        this.x = x;
        this.y = y;
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

    @Override
    public Coordinate clone() {
        return new Coordinate(x, y);
    }

    public boolean equals(Coordinate c) {
        if (c == null) return false;
        return (this.getX() == c.getX()) && (this.getY() == c.getY());
    }

    public void addToX(int x) {
        this.x = this.x + x;
    }

    public void addToY(int y) {
        this.y = this.y + y;
    }
}
