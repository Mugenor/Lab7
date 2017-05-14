package classes;

import java.io.Serializable;

/**
 * Created by Mugenor on 23.02.2017.
 */

abstract public class Human implements Serializable {
    protected static final long serialVersionUID = 42L;
    protected Boolean troublesWithTheLaw=false;
    protected String name;
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
        if (getTroublesWithTheLaw() != human.getTroublesWithTheLaw()) return false;
        return this.getTroublesWithTheLaw() == human.getTroublesWithTheLaw()
                &&this.name !=null?getName().equals(human.getName()):
                human.getName()==null;
    }
    public int hashCode() {
        return 31 * (getTroublesWithTheLaw() ? 1 : 0) + getName().hashCode();
    }
    public String toString(){
        return "Имя: " + this.getName() +
                "\nПроблемы с законом: " + this.getTroublesWithTheLaw();
    }
}
