import java.io.Serializable;

/**
 * Created by Mugenor on 08.05.2017.
 */
public class ConnectionState implements Serializable {
    protected static final long serialVersionUID = 42L;
    public static final int WAITING=20;
    public static final int NEW_DATA=21;
    public static final int DISCONNECT=22;
}
