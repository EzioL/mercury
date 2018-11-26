package socket;

import socket.enmus.MercurySocketServerMode;
import socket.factory.MercurySocketServerModeFactory;
import socket.handler.MercurySocketHandler;

/**
 * Here be dragons
 * Created by haotian on 2018/11/22 4:42 PM
 */

public class MercurySocketServerBuilder {

    public static final int DEFAULT_PORT = 2333;

    private int port = DEFAULT_PORT;

    private MercurySocketServerMode mode = MercurySocketServerMode.CLASSIC_BASIC;

    private MercurySocketHandler mercurySocketHandler;

    //private boolean logEnable;
    //
    //private String logTag = "MercurySocketServer";

    public static MercurySocketServerBuilder newBuilder() {
        return new MercurySocketServerBuilder();
    }

    public MercurySocketServerBuilder port(int port) {
        this.port = port;
        return this;
    }

    public MercurySocketServerBuilder addMercurySocketHandler(MercurySocketHandler mercurySocketHandler) {
        this.mercurySocketHandler = mercurySocketHandler;
        return this;
    }

    public MercurySocketServerBuilder mode(MercurySocketServerMode modeDesign) {
        this.mode = modeDesign;
        return this;
    }

    public MercurySocketServerBuilder build() {
        // TODO: 2018/11/26 not null校验



        switch (mode) {
            case CLASSIC_BASIC:
                MercurySocketServerModeFactory.createClassicBasicHandler(port,mercurySocketHandler).run();
                break;
            case REACTOR_BASIC:
                MercurySocketServerModeFactory.createReactorBasicHandler(port).run();
                break;
        }

        return this;
    }
}
