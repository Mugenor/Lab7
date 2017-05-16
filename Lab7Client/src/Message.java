import classes.NormalHuman;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by Mugenor on 11.05.2017.
 */
public class Message implements Serializable {
    protected static final long serialVersionUID = 42L;
    private byte state;
    private LinkedList<NormalHuman> data;
    private HashSet<Integer> notEditable;
    private byte typeOfOperation;
    public int maxID;
    public static final byte delete = 0;
    public static final byte add = 1;
    public static final byte change = 2;
    public static final byte notEdit = 3;
    public void setTypeOfOperation(byte typeOfOperation) {this.typeOfOperation = typeOfOperation;}
    public byte getTypeOfOperation() {return typeOfOperation;}

    public Message(byte state){
        this.state=state;
        data = new LinkedList<>();
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
    public HashSet<Integer> getNotEditable(){
        return notEditable;
    }
    public void reinitialize(HashSet<Integer> notEditable){
        this.notEditable = new HashSet<>(notEditable);
    }
}
