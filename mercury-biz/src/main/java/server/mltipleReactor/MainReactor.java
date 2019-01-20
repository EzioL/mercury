package server.mltipleReactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Here be dragons Created by @author Ezio on 2019-01-20 02:17
 */
public class MainReactor implements Runnable {

    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;

    public MainReactor(int port) throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress addr = new InetSocketAddress(port);
        serverSocketChannel.socket().bind(addr);
        serverSocketChannel.configureBlocking(false);
        SelectionKey sk = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(new Acceptor(serverSocketChannel));
    }

    @Override
    public void run() {

        while (!Thread.interrupted()) {

            System.out.println("mainReactor waiting for new event on port: "
                + serverSocketChannel.socket().getLocalPort() + "...");
            try {
                if (selector.select() == 0) {
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = selectedKeys.iterator();
            while (it.hasNext()) {
                dispatch((SelectionKey) (it.next()));
                it.remove();
            }
        }
    }

    private void dispatch(SelectionKey key) {
        Runnable r = (Runnable) (key.attachment());
        if (r != null) {
            r.run();
        }
    }
}
