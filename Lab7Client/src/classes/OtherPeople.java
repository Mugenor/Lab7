package classes;

/**
 * Created by Mugenor on 23.02.2017.
 */
public class OtherPeople extends NormalHuman implements CanCatchKarlson{
    protected boolean tryCatchKarlson;
    public OtherPeople(){};
    public OtherPeople(String name) throws KarlsonNameException{
        super(name);
    }
    public void catchKarlson(){
        this.tryCatchKarlson=false;
    }
    public String searchKarlson(){
        this.tryCatchKarlson=true;
        return name+" ���� ��������";
    }
    public boolean isTryCatchKarlson(){
        return this.tryCatchKarlson;
    }
    public boolean equals(Object o){
        if(this==o) return true;
        if(o==null || !(o instanceof OtherPeople)) return false;
        if(!super.equals(o)) return false;
        OtherPeople otherPeople = (OtherPeople) o;
        return this.isTryCatchKarlson() == otherPeople.isTryCatchKarlson();
    }
    public int hashCode(){
        int r = super.hashCode();
        r = 31*r + (this.isTryCatchKarlson()?1:0);
        return r;
    }
    public String toString(){
        return super.toString()+
                "\n�������� ������� ��������: " + this.isTryCatchKarlson();
    }
}
