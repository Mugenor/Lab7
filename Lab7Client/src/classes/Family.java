package classes;

/**
 * Created by Mugenor on 23.02.2017.
 */

public class Family  extends NormalHuman{
    protected boolean peace;
    protected boolean fear;
    protected Fam status;
    protected String decision;
    protected boolean hideKarlson;
    public Family(){
        super();
    }
    public Family(String name, Fam status) throws KarlsonNameException{
        super(name);
        this.status = status;
    }
    public String setPeace(boolean peace){
        this.peace=peace;
        if(!peace){fear=true; return name + " испуглся за свой покой";}
        else {fear=false; return name + " в покое";}
    }
    public boolean getPeace(){
        return this.peace;
    }
    public void setDecision(String decision){
        this.decision=decision;
        System.out.println(name+" решил: " + this.decision);
    }
    public String getDecision(){
        return decision;
    }
    public void setHideKarlson(boolean hidden){
        this.hideKarlson=hidden;
    }
    public boolean isHiddenKarlson() {
        return this.hideKarlson;
    }
    public boolean equals(Object o){
        if(this == o) return true;
        if (o==null || !(o instanceof Family)) return false;
        if(!super.equals(o)) return false;
        Family family = (Family) o;
        return this.getPeace() == family.getPeace()
                && this.isHiddenKarlson() == family.isHiddenKarlson()
                && (this.decision!=null? this.getDecision().equals(family.getDecision())
                : family.getDecision()==null);
    }
    public int hashCode(){
        int r = super.hashCode();
        r= 31*r+(this.getPeace()?1:0);
        r=31*r+(this.isHiddenKarlson()?1:0);
        r=31*r+this.getDecision().hashCode();
        return r;
    }
    public String toString(){
        return super.toString() +
                "\nПокой: " + this.getPeace()+
                "\nРешение: " + this.getDecision() +
                "\nКарлсон спрятан: " + this.isHiddenKarlson();
    }
    public void allThoughts(){
        System.out.println(this.name + " думает, что: ");
        for(Thinkable th: thoughts)
            if(th.toString()!=null) System.out.println(th.toString());
    }
}
