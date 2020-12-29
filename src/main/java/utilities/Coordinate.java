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
}
