/**
 * Created by Mugenor on 08.05.2017.
 */
public class ConnectionState {
    public static final int NEW_DATA=0;
    private int currentState;
    public ConnectionState(int currentState){
        this.currentState=currentState;
    }
    public int getCurrentState(){
        return  currentState;
    }
    public void setCurrentState(int state){
        currentState=state;
    }
    public boolean is(int state){
        return currentState==state;
    }
}
