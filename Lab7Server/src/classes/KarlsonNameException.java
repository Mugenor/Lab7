package classes;

/**
 * Created by Mugenor on 23.02.2017.
 */
public class KarlsonNameException extends Exception {
    public KarlsonNameException(){super("У Карлсона только одно имя!");}
    public KarlsonNameException(String s){super(s);}
}
