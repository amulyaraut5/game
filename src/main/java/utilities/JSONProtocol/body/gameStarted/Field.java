package utilities.JSONProtocol.body.gameStarted;

import java.util.ArrayList;

public class Field {

    private String type;
    private Integer speed;
    private Boolean isCrossing;
    private String orientation;
    private ArrayList<String> orientations;
    private ArrayList<Integer> registers;
    private Integer count;

    public Field(){

    }

    /**
     * Getter and Setter
     */
    public String getType() {
        return type;
    }

    public Integer getSpeed() {
        return speed;
    }

    public Boolean getCrossing() {
        return isCrossing;
    }

    public String getOrientation() {
        return orientation;
    }

    public ArrayList<String> getOrientations() {
        return orientations;
    }

    public ArrayList<Integer> getRegisters() {
        return registers;
    }

    public Integer getCount() {
        return count;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public void setCrossing(Boolean crossing) {
        isCrossing = crossing;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public void setOrientations(ArrayList<String> orientations) {
        this.orientations = orientations;
    }

    public void setRegisters(ArrayList<Integer> registers) {
        this.registers = registers;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
