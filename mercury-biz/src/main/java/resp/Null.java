package resp;

import java.io.Serializable;

/**
 * Here be dragons
 * Created by haotian on 2018/11/7 6:30 PM
 */
public final class Null implements Serializable {
    private static final long serialVersionUID = -1L;
    public static final Null NULL = new Null();
    private Null(){}
}

