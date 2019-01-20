package server.mltipleReactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Here be dragons Created by @author Ezio on 2019-01-20 02:22
 */
public class Handler implements Runnable {

    private final SelectionKey selectionKey;
    private final SocketChannel socketChannel;
    private static final int THREAD_COUNTING = 10;
    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(
        THREAD_COUNTING, THREAD_COUNTING, 10, TimeUnit.SECONDS,
        new LinkedBlockingQueue<Runnable>());

    HandlerState state;

    public Handler(SelectionKey sk, SocketChannel socketChannel) {

        this.selectionKey = sk;
        this.socketChannel = socketChannel;
        state = new ReadState();
        pool.setMaximumPoolSize(32);
    }

    @Override public void run() {

        try {
            state.handle(this, selectionKey, socketChannel, pool);
        } catch (IOException e) {
            System.out.println("A client has been closed.");
            closeChannel();
        }
    }

    public void closeChannel() {
        try {
            selectionKey.cancel();
            socketChannel.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void setState(HandlerState state) {
        this.state = state;
    }
}
