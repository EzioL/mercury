package socket.reactor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Here be dragons
 * Created by @author Ezio on 2018/12/9 7:45 PM
 */
public class MercuryReactor implements Runnable {

    final Selector selector;

    final ServerSocketChannel server;

    public MercuryReactor(int port) throws IOException {

        selector = Selector.open();

        server = ServerSocketChannel.open();

        server.socket().bind(new InetSocketAddress(port));

        server.configureBlocking(false);

        SelectionKey selectionKey = server.register(selector, SelectionKey.OP_ACCEPT);

        selectionKey.attach(new Acceptor());
    }

    @Override
    public void run() {

        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set selected = selector.selectedKeys();
                Iterator iterator = selected.iterator();

                while (iterator.hasNext()) {
                    dispatch((SelectionKey) iterator.next());
                }

                selected.clear();
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

    class Acceptor implements Runnable {

        @Override
        public void run() {

            try {
                SocketChannel socketChannel = server.accept();

                if (socketChannel != null) {
                    System.err.println("acceptor —> " + socketChannel);
                    new Handler(selector, socketChannel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Handler implements Runnable {

        final SocketChannel socket;
        final SelectionKey selectionKey;
        private int MAXIN = 1024 * 1;
        private int MAXOUT = 1024 * 1;
        ByteBuffer input = ByteBuffer.allocate(MAXIN);

        ByteBuffer output = ByteBuffer.allocate(MAXOUT);

        static final int READING = 0, SENDING = 1;
        int state = READING;

        public Handler(Selector selector, SocketChannel socketChannel) throws IOException {

            this.socket = socketChannel;
            socketChannel.configureBlocking(false);
            selectionKey = socketChannel.register(selector, 0);
            selectionKey.attach(this);
            selectionKey.interestOps(SelectionKey.OP_READ);
            selector.wakeup();
        }

        boolean inputIsComplete() {

            return true;
        }

        boolean outputIsComplete() {

            return true;
        }

        @Override
        public void run() {

            try {
                if (state == READING) {
                    read();
                } else if (state == SENDING) {
                    send();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void read() throws IOException {

            socket.read(input);
            byte[] bytes = new byte[input.remaining()];
            input.get(bytes);
            System.err.println("Client send msg -> " + new String(bytes,"utf-8"));
            if (inputIsComplete()) {
                process();
                state = SENDING;
                // Normally also do first write now
                selectionKey.interestOps(SelectionKey.OP_WRITE);
            }
        }

        private void send() throws IOException {
            socket.write(output);
            if (outputIsComplete()) {
                selectionKey.cancel();
            }
        }

        private void process() {

            try {
                output.put("Hello Client".getBytes("UTF-8"));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
