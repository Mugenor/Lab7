package classes;

import myAnnotations.Column;
import myAnnotations.Id;

import java.io.Serializable;

/**
 * Created by Mugenor on 23.02.2017.
 */

abstract public class Human implements Serializable {
    protected static final long serialVersionUID = 42L;
    @Column(name="troubleswiththelaw")
    protected Boolean troublesWithTheLaw=false;
    @Column(name="name")
    protected String name;
    @Id
    @Column(name="id")
    protected int id;
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getName(){
        return this.name;
    }
    public Human(String name, boolean troubles) throws KarlsonNameException{
        if(name.equals("Karlson")) throw new KarlsonNameException();
        this.name=name;
        this.troublesWithTheLaw=troubles;
    }
    public Human(String name) throws KarlsonNameException{
        if(name.equals("Karlson")) throw new KarlsonNameException();
        this.name=name;
    }
    public Human(){}
    public void setTroublesWithTheLaw(boolean troublesWithTheLaw) {
        this.troublesWithTheLaw = troublesWithTheLaw;
    }
    public Boolean getTroublesWithTheLaw(){
        return this.troublesWithTheLaw;
    }
    public void setName(String name) throws KarlsonNameException{
        if(name.equals("Karlson")) throw new KarlsonNameException();
        this.name=name;
    }
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o==null || !(o instanceof Human)) return false;
        Human human = (Human) o;
        return this.id == human.getId();
    }
    public int hashCode() {
        return 31 * (getTroublesWithTheLaw() ? 1 : 0) + getName().hashCode() + id;
    }
    public String toString(){
        return "Id: " + this.getId() + "\nИмя: " + this.getName() +
                "\nПроблемы с законом: " + this.getTroublesWithTheLaw();
    }
}
