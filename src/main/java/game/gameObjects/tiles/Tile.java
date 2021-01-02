package game.gameObjects.tiles;

import utilities.Utilities;

import java.util.ArrayList;

public class Tile {

    private static Tile instance;
    protected String imagePath;
    /**
     * saves one or multiple Attributes for one tile
     */
    private ArrayList<Attribute> attributes = new ArrayList<Attribute>();

    /**
     * Constructor for tiles.
     */
    private Tile() {
    }

    //private Image image;

    public static Tile getInstance() {
        if (instance == null) {
            instance = new Tile();
        }
        return instance;
    }

	/*
	// Alternative for GraphicsContext for rendering images of Tile
	// Constructor of Tile takes two parameter

	    public Tile(Attribute attribute, Image image){
	       this.attribute1 = attribute;
	       this.image = image;

	 // Getter for image
	     public Image getImage(){
	        return this.image;
	     }

	  // While creating tile, we set path for new Image, which gonna load the image
	  // Since we have individual images we can even resize the image, which takes additional two parameters

	  Example:

	   public Tile createTile(int tileID){
	       Tile tile = null;
	       switch(tileID){
	           case 00 : tile = new Tile(new Empty(), new Image("imagepath"));
	       }
	   }
	 */

    public ArrayList<Attribute> getAttribute() {
        return attributes;
    }

    /**
     * Adds an Attribute to a tile by saving it in the ArrayList of attributes
     *
     * @param attribute added Attribute
     */
    public void addAttribute(Attribute attribute) {
        attributes.add(attribute);
    }

    /**
     * It sets the priority order of execution of different attributes of tiles.
     */
    public void priorityOrder() {
        //TODO
    }

    /**
     * TODO
     *
     * @param gc
     * @param position
     */

    /**
     * This method creates the tile with specific attribute.
     * Every tile has then it's own specific id which can be called while laying out the map structure.
     * Depending upon the needs of map, we can create our own tile with multiple attributes with different orientations.
     *
     * @param tileID
     * @return tile
     */
    public Tile createTile(int tileID) {
        Tile tile = new Tile();
        ArrayList<Utilities.Orientation> orientations;
        switch (tileID) {
            case 00:
                tile.addAttribute(new Empty());
                break;

            case 111:
                tile.addAttribute(new Antenna());
                break;
            // draw();
            // TODO should be changed
            // Belt constructor has 2 attributes.

            //Green Conveyor Belts
            case 01:
                tile.addAttribute(new Belt(Utilities.Orientation.UP, 1));
                break;
            case 02:
                tile.addAttribute(new Belt(Utilities.Orientation.DOWN, 1));
                break;
            case 03:
                tile.addAttribute(new Belt(Utilities.Orientation.LEFT, 1));
                break;
            case 04:
                tile.addAttribute(new Belt(Utilities.Orientation.RIGHT, 1));
                break;
            // draw();

            //Blue Conveyor Belts
            case 11:
                tile.addAttribute(new Belt(Utilities.Orientation.UP, 2));
                break;
            case 12:
                tile.addAttribute(new Belt(Utilities.Orientation.DOWN, 2));
                break;
            case 13:
                tile.addAttribute(new Belt(Utilities.Orientation.LEFT, 2));
                break;
            case 14:
                tile.addAttribute(new Belt(Utilities.Orientation.RIGHT, 2));
                break;
            // draw();

            //Green Rotating Conveyor Belts
            case 21:
                tile.addAttribute(new RotatingBelt(Utilities.UP_LEFT, false, 1));
                break;
            case 22:
                tile.addAttribute(new RotatingBelt(Utilities.UP_RIGHT, false, 1));
                break;
            case 23:
                tile.addAttribute(new RotatingBelt(Utilities.DOWN_LEFT, false, 1));
                break;
            case 24:
                tile.addAttribute(new RotatingBelt(Utilities.DOWN_RIGHT, false, 1));
                break;

            case 25:
                tile.addAttribute(new RotatingBelt(Utilities.UP_LEFT, true, 1));
                break;
            case 26:
                tile.addAttribute(new RotatingBelt(Utilities.UP_RIGHT, true, 1));
                break;
            case 27:
                tile.addAttribute(new RotatingBelt(Utilities.DOWN_LEFT, true, 1));
                break;
            case 28:
                tile.addAttribute(new RotatingBelt(Utilities.DOWN_RIGHT, true, 1));
                break;
            // draw();

            //Blue Rotating Conveyor Belts
            case 31:
                tile.addAttribute(new RotatingBelt(Utilities.UP_LEFT, false, 2));
                break;
            case 32:
                tile.addAttribute(new RotatingBelt(Utilities.UP_RIGHT, false, 2));
                break;
            case 33:
                tile.addAttribute(new RotatingBelt(Utilities.DOWN_LEFT, false, 2));
                break;
            case 34:
                tile.addAttribute(new RotatingBelt(Utilities.DOWN_RIGHT, false, 2));
                break;

            case 101:
                tile.addAttribute(new RotatingBelt(Utilities.LEFT_UP, false, 2));
                break;
            case 102:
                tile.addAttribute(new RotatingBelt(Utilities.RIGHT_UP, false, 2));
                break;
            case 103:
                tile.addAttribute(new RotatingBelt(Utilities.LEFT_DOWN, false, 2));
                break;
            case 104:
                tile.addAttribute(new RotatingBelt(Utilities.RIGHT_DOWN, false, 2));
                break;

            case 35:
                tile.addAttribute(new RotatingBelt(Utilities.UP_LEFT, true, 2));
                break;
            case 36:
                tile.addAttribute(new RotatingBelt(Utilities.UP_RIGHT, true, 2));
                break;
            case 37:
                tile.addAttribute(new RotatingBelt(Utilities.DOWN_LEFT, true, 2));
                break;
            case 38:
                tile.addAttribute(new RotatingBelt(Utilities.DOWN_RIGHT, true, 2));
                break;

            case 105:
                tile.addAttribute(new RotatingBelt(Utilities.LEFT_UP, true, 2));
                break;
            case 106:
                tile.addAttribute(new RotatingBelt(Utilities.RIGHT_UP, true, 2));
                break;
            case 107:
                tile.addAttribute(new RotatingBelt(Utilities.LEFT_DOWN, true, 2));
                break;
            case 108:
                tile.addAttribute(new RotatingBelt(Utilities.RIGHT_DOWN, true, 2));
                break;
            // draw();

            //Push Panels
            case 40:
                tile.addAttribute(new PushPanel(Utilities.Orientation.DOWN, new int[]{2, 4}));
                break;
            case 41:
                tile.addAttribute(new PushPanel(Utilities.Orientation.UP, new int[]{2, 4}));
                break;
            case 42:
                tile.addAttribute(new PushPanel(Utilities.Orientation.LEFT, new int[]{2, 4}));
                break;
            case 43:
                tile.addAttribute(new PushPanel(Utilities.Orientation.RIGHT, new int[]{2, 4}));
                break;

            case 44:
                tile.addAttribute(new PushPanel(Utilities.Orientation.DOWN, new int[]{1, 3, 5}));
                break;
            case 45:
                tile.addAttribute(new PushPanel(Utilities.Orientation.UP, new int[]{1, 3, 5}));
                break;
            case 46:
                tile.addAttribute(new PushPanel(Utilities.Orientation.LEFT, new int[]{1, 3, 5}));
                break;
            case 47:
                tile.addAttribute(new PushPanel(Utilities.Orientation.RIGHT, new int[]{1, 3, 5}));
                break;

            //Gears
            case 51:
                tile.addAttribute(new Gear(Utilities.Orientation.LEFT));
                break;
            case 52:
                tile.addAttribute(new Gear(Utilities.Orientation.RIGHT));
                break;
            // draw();

            //Board Lasers
            case 61:
                tile.addAttribute(new Laser(Utilities.Orientation.DOWN, 1));
                break;
            case 62:
                tile.addAttribute(new Laser(Utilities.Orientation.UP, 1));
                break;
            case 63:
                tile.addAttribute(new Laser(Utilities.Orientation.LEFT, 1));
                break;
            case 64:
                tile.addAttribute(new Laser(Utilities.Orientation.RIGHT, 1));
                break;

            case 65:
                tile.addAttribute(new Laser(Utilities.Orientation.DOWN, 2));
                break;
            case 66:
                tile.addAttribute(new Laser(Utilities.Orientation.UP, 2));
                break;
            case 67:
                tile.addAttribute(new Laser(Utilities.Orientation.LEFT, 2));
                break;
            case 68:
                tile.addAttribute(new Laser(Utilities.Orientation.RIGHT, 2));
                break;

            case 71:
                tile.addAttribute(new Laser(Utilities.Orientation.DOWN, 3));
                break;
            case 72:
                tile.addAttribute(new Laser(Utilities.Orientation.UP, 3));
                break;
            case 73:
                tile.addAttribute(new Laser(Utilities.Orientation.LEFT, 3));
                break;
            case 74:
                tile.addAttribute(new Laser(Utilities.Orientation.RIGHT, 3));
                break;
            // draw();

            //Energy Spaces
            case 81:
                tile.addAttribute(new EnergySpace(0));
                break;
            case 82:
                tile.addAttribute(new EnergySpace(1));
                break;
            // draw();

            //Pits
            case 90:
                tile.addAttribute(new Pit());
                break;

            //Walls
            case 91:
                tile.addAttribute(new Wall(Utilities.Orientation.DOWN));
                break;
            case 92:
                tile.addAttribute(new Wall(Utilities.Orientation.UP));
                break;
            case 93:
                tile.addAttribute(new Wall(Utilities.Orientation.LEFT));
                break;
            case 94:
                tile.addAttribute(new Wall(Utilities.Orientation.RIGHT));
                break;

            case 95:
                tile.addAttribute(new Wall(Utilities.UP_LEFT));
                break;
            case 96:
                tile.addAttribute(new Wall(Utilities.UP_RIGHT));
                break;
            case 97:
                tile.addAttribute(new Wall(Utilities.DOWN_LEFT));
                break;
            case 98:
                tile.addAttribute(new Wall(Utilities.DOWN_RIGHT));
                break;

            // Tile with two walls

        }
        return tile;
    }
}