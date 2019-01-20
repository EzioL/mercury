package server.mode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
import server.handler.MercuryServerHandler;
import server.handler.MercurySocketServerHandler;
import server.reactor.MercuryAcceptor;

/**
 * Here be dragons
 *
 * @author haotian
 */
public class ReactorMercurySocketServerHandler extends MercurySocketServerHandler {

    private MercuryServerHandler serverHandler;

    private int port;

    private Selector selector;

    private ServerSocketChannel server;

    public ReactorMercurySocketServerHandler(int port, MercuryServerHandler serverHandler) {
        this.port = port;
        this.serverHandler = serverHandler;
    }

    @Override
    public void run() {
        try {
            selector = Selector.open();
            server = ServerSocketChannel.open();
            server.socket().bind(new InetSocketAddress(port));
            server.configureBlocking(false);
            SelectionKey selectionKey = server.register(selector, SelectionKey.OP_ACCEPT);
            selectionKey.attach(new MercuryAcceptor(selector,server,serverHandler));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();

                while (iterator.hasNext()) {
                    dispatch(iterator.next());
                }
                selectedKeys.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatch(SelectionKey selectionKey) {
        Runnable runnable = (Runnable) selectionKey.attachment();
        if (runnable != null) {
            runnable.run();
        }
    }
}
