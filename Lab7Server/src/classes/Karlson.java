package classes;

/**
 * Created by Mugenor on 23.02.2017.
 */
public class Karlson extends Human {
    protected boolean spy;
    protected boolean discovered;
    public int ar[]={1, 2, 3, 4, 5};
    public Karlson() {
        name="Karlson";
    }
    public void setName(String name) throws KarlsonNameException {
        throw new KarlsonNameException();
    }
    public void setSpy(boolean spy) {
        this.spy=spy;
    }
    public String toStringSetSpy(boolean spy) {
        this.spy = spy;
        if(this.spy)return name+" на самом деле шпион";
        else return name+" на самом деле не шпион";
    }
    public boolean isSpy() {
        return this.spy;
    }
    public String setDiscover(boolean discover) {
        this.discovered = discover;
        if(discover) return name+" не в безопасности";
        else return name+" в безопасности";
    }
    public boolean isDiscovered() {
        return this.discovered;
    }
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Karlson)) return false;
        if (!super.equals(o)) return false;
        Karlson karlson = (Karlson) o;
        return this.isDiscovered() == karlson.isDiscovered()&&
                this.isSpy()==karlson.isSpy();
    }
    public int hashCode() {
        int r = super.hashCode();
        r = 31*r+(this.isSpy()? 1: 0);
        r= 31*r + (this.isDiscovered()? 1: 0);
        return r;
    }
    public String toString(){
        return  super.toString() +
                "\nШпион: " + this.isSpy() +
                "\nРассекречен: " + this.isDiscovered();
    }
}