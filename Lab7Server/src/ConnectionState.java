import java.io.Serializable;

/**
 * Created by Mugenor on 08.05.2017.
 */
public class ConnectionState implements Serializable {
    protected static final long serialVersionUID = 42L;
    public static final byte NEW_DATA=21;
    public static final byte DISCONNECT=22;
    public static final byte NEED_DATA=23;
    public static final byte FINAL_ITERATE=-1;
    public static final byte READ=24;
    public static final byte ERROR=25;
}
