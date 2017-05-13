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
    private long id;
    public Message(byte state, LinkedList<NormalHuman> data){
        this.state=state;
        this.data=data;
        id=-1;
    }
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
    public void updateID(){
        id++;
    }
    public long getID(){
        return id;
    }
    public void clearData(){data=null;}
    public boolean isData(){return data!=null;}
}
