package socket.entity;

import lombok.NonNull;

/**
 * Here be dragons
 * Created by haotian on 2018/11/20 12:04 PM
 */
public enum MercuryCommand {
    HEARTBEAT(100, "心跳"),

    PUT_DATA(300, "存储数据"),

    GET_DATA(400, "获取数据"),

    DELETE_DATA(500, "删除数据"),

    NOTIFY_DISCONNECT(3000, "服务器将要断连的通知")
    ;

    private int value;

    @NonNull
    private String desc;

    MercuryCommand(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return this.value;
    }

    public String getDesc() {
        return desc;
    }

    public static MercuryCommand fromValue(int value) {
        for (MercuryCommand mercuryCommand : values()) {
            if (mercuryCommand.getValue() == value) {
                return mercuryCommand;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "MercuryCommand{" +
            "value=" + value +
            ", desc='" + desc + '\'' +
            '}';
    }
}
