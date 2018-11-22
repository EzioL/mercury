package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Here be dragons
 * Created by haotian on 2018/11/22 4:42 PM
 */

public class MercurySocketServerBuilder {

    public static final int DEFAULT_PORT = 2333;

    private int port = DEFAULT_PORT;

    private MercurySocketServerMode mode = MercurySocketServerMode.CLASSIC_BASIC;

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

    public MercurySocketServerBuilder mode(MercurySocketServerMode modeDesign) {
        this.mode = modeDesign;
        return this;
    }

    public MercurySocketServerBuilder build() throws IOException {

        MercuryServerSocket mercuryServerSocket = new MercuryServerSocket(this.port);
        Socket accept = mercuryServerSocket.accept();
        switch (mode) {
            case CLASSIC_BASIC:

                break;
        }

        return this;
    }

    private class MercuryServerSocket extends ServerSocket {

        public MercuryServerSocket(int port) throws IOException {
            super(port);
        }
    }
}
