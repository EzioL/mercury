package enmus;

/**
 * Here be dragons
 * Created by haotian on 2018/11/22 5:53 PM
 */
public enum MercurySocketServerMode {

    CLASSIC_BASIC("CLASSIC", "Each socket handler may be started in its own thread"),
    CLASSIC_THREAD_POOL("CLASSIC", "Each socket handler may be started in its own thread"),
    REACTOR_BASIC("BASIC_REACTOR", "I don't know what it is now."),
    REACTOR_MULTITHREADED("BASIC_REACTOR", "I don't know what it is now."),
    ;

    private String mode;
    private String desc;

    MercurySocketServerMode(String mode, String desc) {
        this.mode = mode;
        this.desc = desc;
    }

    public String getMode() {
        return mode;
    }

    public String getDesc() {
        return desc;
    }

}
