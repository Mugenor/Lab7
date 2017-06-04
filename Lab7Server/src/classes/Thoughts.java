package classes;

import myAnnotations.Column;
import myAnnotations.Table;

/**
 * Created by Mugenor on 04.06.2017.
 */
@Table(name="thoughts")
public class Thoughts implements Thinkable {
    @Column(name="thought")
    protected String thought;
    public Thoughts(){}
    public void setThought(String th){thought=th;}
    public String getThougth(){return thought;}
    public void thinkAbout(String th){
        thought=th;
    }
    public void thinkAbout(Thinkable th){
        thought=th.toString();
    }
    public void forgetIt(){
        this.thought=null;
    }
    public boolean equals(Object th) {
        if (this == th) return true;
        if (th == null || !(th instanceof Thoughts)) return false;
        Thoughts thoughts = (Thoughts) th;
        return thought.equals(thoughts.thought);
    }
    public int hashCode() {
        return thought != null ? thought.hashCode() : 0;
    }
    public String toString(){
        return this.thought;
    }
}
