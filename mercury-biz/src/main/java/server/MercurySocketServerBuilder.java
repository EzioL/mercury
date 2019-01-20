package server;

import server.enmus.MercurySocketServerMode;
import server.factory.MercurySocketServerModeFactory;
import server.handler.MercuryServerHandler;

/**
 * Here be dragons Created by haotian on 2018/11/22 4:42 PM
 */

public class MercurySocketServerBuilder {

    public static final int DEFAULT_PORT = 2333;

    private int port = DEFAULT_PORT;

    private MercurySocketServerMode mode = MercurySocketServerMode.CLASSIC_BASIC;

    private MercuryServerHandler serverHandler;

    public static MercurySocketServerBuilder newBuilder() {
        return new MercurySocketServerBuilder();
    }

    public MercurySocketServerBuilder port(int port) {
        this.port = port;
        return this;
    }

    public MercurySocketServerBuilder addServerHandler(MercuryServerHandler serverHandler) {
        this.serverHandler = serverHandler;
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
                MercurySocketServerModeFactory.createClassicBasicHandler(port, serverHandler).run();
                break;
            case CLASSIC_THREAD_POOL:
                break;
            case REACTOR_BASIC:
                if (serverHandler == null) {
                    throw new RuntimeException("serverHandler is null");
                }
                MercurySocketServerModeFactory.createReactorBasicHandler(port, serverHandler).run();
                break;
            case REACTOR_MULTITHREADED:
                // MercurySocketServerModeFactory.createReactorBasicHandlerWithThreadPool(port, serverHandler).run();
                break;
            default:
                break;
        }

        return this;
    }
}
