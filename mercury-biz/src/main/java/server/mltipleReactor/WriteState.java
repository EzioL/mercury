package server.mltipleReactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Here be dragons Created by @author Ezio on 2019-01-20 10:39
 */
public class WriteState implements HandlerState {

    public WriteState() {
    }

    @Override
    public void changeState(Handler handler) {
        handler.setState(new ReadState());
    }

    @Override public void handle(Handler handler, SelectionKey selectionKey, SocketChannel socketChannel, ThreadPoolExecutor pool)
        throws IOException {

        String str = "Your message has sent to "
            + socketChannel.socket().getLocalSocketAddress().toString() + "\r\n";
        ByteBuffer buf = ByteBuffer.wrap(str.getBytes());

        while (buf.hasRemaining()) {
            socketChannel.write(buf);
        }

        handler.setState(new ReadState());
        selectionKey.interestOps(SelectionKey.OP_READ);

    }
}
