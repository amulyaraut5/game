package game;

public  class Card {
    public static final int PRINCESS = 8;
    public static final int COUNTESS = 7;
    public static final int KING = 6;
    public static final int PRINCE = 5;
    public static final int MAID = 4;
    public static final int BARON = 3;
    public static final int PRIEST = 2;
    public static final int GUARD = 1;


    // array at index 0 is not the card
    public final static int[] numberOfIndividualCards = {
            0, 5, 2, 2, 2, 2, 1, 1, 1
    };
    public static final String[] nameOfCards = {
            "", "Guard", "Priest", "Baron", "Handmaid", "Prince", "King", "Countess", "Princess"};


    public int value;   // card's value as printed on the card. (1-8)

    public Card(int value) {
        this.value = value;
    }

    public String toString() {
        return Integer.toString(value);
    }

    public void handleCard() {

    }

}