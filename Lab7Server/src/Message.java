import classes.NormalHuman;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by Mugenor on 11.05.2017.
 */
public class Message implements Serializable {
    protected static final long serialVersionUID = 42L;
    private byte state;
    private LinkedList<NormalHuman> data;
    private byte typeOfOperation;
    public int maxID;
    public static final byte delete = 0;
    public static final byte add = 1;
    public static final byte change = 2;
    public void setTypeOfOperation(byte typeOfOperation) {this.typeOfOperation = typeOfOperation;}
    public int getTypeOfOperation() {return typeOfOperation;}
    public Message(byte state, LinkedList<NormalHuman> data){
        this.state=state;
        this.data=data;
    }
    public Message(byte state){
        this.state=state;
        data = new LinkedList<>();
        maxID=Main.maxID;
    }
    public LinkedList<NormalHuman> getData(){
        return data;
    }
    public void setState(byte state){
        this.state=state;
    }
    public byte getState(){
        return state;
    }
    public void setData(LinkedList<NormalHuman> data){
        this.data=data;
    }
    public void clearData(){data=null;}
    public boolean isData(){return data!=null;}
}
