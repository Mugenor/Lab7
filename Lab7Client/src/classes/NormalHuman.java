package classes;
import myAnnotations.*;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;

@Table(name="normalhuman")
public class NormalHuman extends Human implements Comparable<NormalHuman>{
    @Column(name="timeOfCreate")
    @DateTime
    protected ZonedDateTime timeOfCreate;
    @Column(name="age")
    protected Long age=1l;
    @Property(type="classes.Thoughts" , refColumn = "id")
    protected ArrayList<Thoughts> thoughts;
    public NormalHuman(String name) throws KarlsonNameException{
        super(name);
        this.thoughts = new ArrayList<Thoughts>();
        Instant time = Instant.now();
        timeOfCreate = ZonedDateTime.ofInstant(time, ZoneOffset.UTC);
    }
    public NormalHuman(){
        super();
        this.thoughts = new ArrayList<Thoughts>();
        Instant time = LocalDateTime.now().toInstant(ZoneOffset.UTC);
        timeOfCreate = ZonedDateTime.ofInstant(time, ZoneOffset.UTC);
        System.out.println(timeOfCreate);
        System.out.println(time);
    }
    public ZonedDateTime getTimeOfCreate(){return timeOfCreate;}
    public void setTimeOfCreate() {timeOfCreate = ZonedDateTime.now();}
    public String getThoughts(int i) {
        if (i <= thoughts.size() && i >= 0) {
            return thoughts.get(i).toString();
        } else throw new ThoughtIndexException();
    }
    public void forgetThought(int i){thoughts.remove(i);}
    public Long getAge(){
        return age;
    }
    public void setAge(long age){
        this.age=age;
    }
    public int getThoughtsCount(){
        return thoughts.size();
    }
    public void thinkAbout(String th){
        Thoughts thought = new Thoughts();
        thought.thinkAbout(th);
        thoughts.add(thought);
    }
    public void setThoughts(ArrayList<Thoughts> thoughts){
        this.thoughts=thoughts;
    }
    public ArrayList<Thoughts> getThoughts(){return thoughts;}
    public void thinkAbout(String th, int i){
        if(i<=thoughts.size() && i>=0){
            Thoughts thought = new Thoughts();
            thought.thinkAbout(th);
            thoughts.add(i, thought);}
        else throw new ThoughtIndexException();
    }
    public void thinkAbout(Thoughts th, int i){
        if(i<=thoughts.size() && i>=0)thoughts.add(th);
        else throw new ThoughtIndexException();
    }
    public void thinkAbout(Thoughts th){
        thoughts.add(th);
    }
    public ArrayList getAllThoughts(){
        return thoughts;
    }
    public int hashCode() {
        int r = super.hashCode();
        r = 31*r+ thoughts.hashCode();
        return r;
    }
    public String toString() {
        return super.toString() +
                "\nГоловные мысли: " + thoughts.toString() +
                "\nВозраст: " + age;
    }
    public int countOfThoughts(){return thoughts.size();}
    public int compareTo(NormalHuman nh){
        return super.name.length()-nh.getName().length()+countOfThoughts()-nh.countOfThoughts()+(troublesWithTheLaw ? 10: -10) - (nh.getTroublesWithTheLaw() ? 10: -10);
    }
    public boolean equals(Object nh){
        return super.equals(nh);
    }
}