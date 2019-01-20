package server.mltipleReactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Here be dragons Created by @author Ezio on 2019-01-20 10:32
 */
public class WorkState implements HandlerState {

    @Override public void changeState(Handler h) {

    }

    @Override public void handle(Handler h, SelectionKey sk, SocketChannel sc, ThreadPoolExecutor pool) throws IOException {

    }
}
