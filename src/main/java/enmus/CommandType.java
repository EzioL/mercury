package enmus;

/**
 * Here be dragons
 * Created by haotian on 2018/11/12 12:15 PM
 */
public enum CommandType {

    HELLO("$HELLO", "Welcome to Mercury"),
    SET("$SET", ""),
    GET("$GET", ""),
    DELETE("$DELETE", ""),
    ;

    private String cmd;
    private String desc;

    CommandType(String cmd, String desc) {
        this.cmd = cmd;
        this.desc = desc;
    }

    public String getCmd() {
        return cmd;
    }

    public String getDesc() {
        return desc;
    }

}
