package socket.reactor;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import socket.handler.MercuryServerHandler;

/**
 * Here be dragons Created by @author Ezio on 2019-01-19 22:44
 */
public class MercuryAcceptor implements Runnable {

    private Selector selector;

    private ServerSocketChannel server;

    private MercuryServerHandler serverHandler;

    public MercuryAcceptor(Selector selector, ServerSocketChannel server, MercuryServerHandler serverHandler) {

        this.selector = selector;
        this.server = server;
        this.serverHandler = serverHandler;
    }

    @Override
    public void run() {
        try {
            SocketChannel socketChannel = server.accept();
            if (socketChannel != null) {
                System.err.println("MercuryAcceptor —> " + socketChannel);
                new MercuryHandler(selector, socketChannel,serverHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
