package utilities.JSONProtocol.body.gameStarted;

import java.util.ArrayList;
import utilities.JSONProtocol.body.gameStarted.Field;

public class Maps {
    private int position;
    private ArrayList<Field> field;

    public Maps(int position, ArrayList<Field> field){
        this.position = position;
        this.field = field;
    }
}
