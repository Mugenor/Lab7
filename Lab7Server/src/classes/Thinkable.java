package classes;

import java.io.Serializable;

/**
 * Created by Mugenor on 23.02.2017.
 */
public interface Thinkable extends Serializable {
    static final long serialVersionUID = 42L;
    void thinkAbout(String thought);
    void forgetIt();
}

