package socket.domain;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Here be dragons
 * Created by haotian on 2018/11/23 6:38 PM
 */
public class MercuryServerSocket extends ServerSocket {

    public MercuryServerSocket(int port) throws IOException {
        super(port);
    }
}
