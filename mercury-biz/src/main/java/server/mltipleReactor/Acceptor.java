package server.mltipleReactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Here be dragons Created by @author Ezio on 2019-01-20 02:18
 */
public class Acceptor implements Runnable {

    private final ServerSocketChannel serverSocketChannel;
    private final int cores = Runtime.getRuntime().availableProcessors();
    private final Selector[] selectors = new Selector[cores];
    private int selIdx = 0;
    private SubReactor[] subReactors = new SubReactor[cores];

    private Thread[] threads = new Thread[cores];

    public Acceptor(ServerSocketChannel serverSocketChannel) throws IOException {

        this.serverSocketChannel = serverSocketChannel;
        // 創建多個selector以及多個subReactor線程
        for (int i = 0; i < cores; i++) {
            selectors[i] = Selector.open();
            subReactors[i] = new SubReactor(selectors[i], serverSocketChannel, i);
            threads[i] = new Thread(subReactors[i]);
            threads[i].start();
        }
    }

    @Override
    public synchronized void run() {
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();

            if (socketChannel != null) {
                socketChannel.configureBlocking(false);
                subReactors[selIdx].setRestart(true);

                selectors[selIdx].wakeup();
                SelectionKey sk = socketChannel.register(selectors[selIdx],
                    SelectionKey.OP_READ);
                selectors[selIdx].wakeup();
                subReactors[selIdx].setRestart(false);
                sk.attach(new Handler(sk, socketChannel));
                if (++selIdx == selectors.length) {
                    selIdx = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
