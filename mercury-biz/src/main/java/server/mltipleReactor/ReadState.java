package server.mltipleReactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.commons.lang3.StringUtils;

/**
 * Here be dragons Created by @author Ezio on 2019-01-20 02:29
 */
public class ReadState implements HandlerState {

    private SelectionKey selectionKey;

    public ReadState() {
    }

    @Override
    public void changeState(Handler handler) {
        handler.setState(new WorkState());
    }

    @Override
    public void handle(Handler handler, SelectionKey selectionKey, SocketChannel socketChannel, ThreadPoolExecutor pool) throws IOException {

        this.selectionKey = selectionKey;

        // non-blocking下不可用Readers，因為Readers不支援non-blocking

        byte[] arr = new byte[1024];
        ByteBuffer buf = ByteBuffer.wrap(arr);
        int numBytes = socketChannel.read(buf);
        if (numBytes == -1) {
            handler.closeChannel();
            return;
        }

        String read = new String(arr, StandardCharsets.UTF_8);

        if (StringUtils.isNotBlank(read)) {

            handler.setState(new WorkState());
            // do process in worker thread
            pool.execute(new WorkerThread(handler, read));
            System.out.println(socketChannel.socket().getRemoteSocketAddress().toString()
                + " > " + read);
        }
    }

    private synchronized void process(Handler handler, String read) {

        // do process(decode, logically process, encode)..

        handler.setState(new WriteState());
        this.selectionKey.interestOps(SelectionKey.OP_WRITE);
        this.selectionKey.selector().wakeup();
    }

    class WorkerThread implements Runnable {

        Handler handler;
        String read;

        public WorkerThread(Handler handler, String streadr) {
            this.handler = handler;
            this.read = read;
        }

        @Override
        public void run() {
            process(handler, read);
        }
    }
}
