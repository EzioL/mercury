package server.mltipleReactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Here be dragons Created by @author Ezio on 2019-01-20 02:27
 */
public interface HandlerState {
    public void changeState(Handler handler);

    public void handle(Handler handler, SelectionKey selectionKey, SocketChannel socketChannel, ThreadPoolExecutor pool) throws IOException;
}
