package exceptions;

/**
 * Created by Mugenor on 02.06.2017.
 */
public class ORMException extends Exception {
    public ORMException(){}
    public ORMException(String exc){
        super(exc);
    }
}
