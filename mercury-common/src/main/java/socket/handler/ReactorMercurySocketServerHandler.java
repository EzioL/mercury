package socket.handler;

import socket.domain.MercuryServerSocket;

/**
 * Here be dragons
 * Created by haotian on 2018/11/23 6:47 PM
 */
public class ReactorMercurySocketServerHandler extends MercurySocketServerHandler {

    private MercuryServerSocket mercuryServerSocket;

    private int port;

    public ReactorMercurySocketServerHandler(int port) {
        this.port = port;
    }

    @Override
    public void run() {


        // TODO: 2018/11/23
    }
}