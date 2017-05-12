import classes.NormalHuman;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by Mugenor on 11.05.2017.
 */
public class Message implements Serializable {
    protected static final long serialVersionUID = 42L;
    private int state;
    private LinkedList<NormalHuman> data;
    public Message(int state, LinkedList<NormalHuman> data){
        this.state=state;
        this.data=data;
    }
    public Message(int state){
        this.state=state;
        data = new LinkedList<>();
    }
    public LinkedList<NormalHuman> getData(){
        return data;
    }
    public void setState(int state){
        this.state=state;
    }
    public int getState(){
        return state;
    }
    public void setData(LinkedList<NormalHuman> data){
        this.data=data;
    }
}
