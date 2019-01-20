package server.reactor;

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
        private final int MAXIN = 1024;
        private final int MAXOUT = 1024;
        private int read = 0;

        ByteBuffer input = ByteBuffer.allocate(MAXIN);

        ByteBuffer output = ByteBuffer.allocate(MAXOUT);

        public Handler(Selector selector, SocketChannel socketChannel) throws IOException {
            this.socket = socketChannel;
            socketChannel.configureBlocking(false);
            selectionKey = socketChannel.register(selector, 0);
            selectionKey.attach(this);
            selectionKey.interestOps(SelectionKey.OP_READ);
            selector.wakeup();
        }

        boolean inputIsComplete() {
            System.err.println("read state ->" + read);
            return true;
        }

        boolean outputIsComplete() {

            return true;
        }

        @Override
        public void run() {
            try {
                read = socket.read(input);
                if (inputIsComplete()) {
                    process();
                    selectionKey.attach(new Sender());
                    selectionKey.interestOps(SelectionKey.OP_WRITE);
                    selectionKey.selector().wakeup();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void process() {
            try {

                String content = System.currentTimeMillis() + " 来自客户端的: ";
                input.flip();
                // =====取出buffer里的数据// 创建字节数组
                byte[] bytes = new byte[input.remaining()];
                // 将数据取出放到字节数组里
                input.get(bytes);
                content += new String(bytes, "UTF-8");
                content += "--------";
                System.out.println(content);

                byte[] resp = "Hello Client".getBytes("UTF-8");

                output = ByteBuffer.wrap(resp);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        class Sender implements Runnable {

            @Override public void run() {

                try {

                    socket.write(output);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (outputIsComplete()) {
                    selectionKey.cancel();
                }
            }
        }

        //
        //private void read() throws IOException {
        //
        //    socket.read(input);
        //    byte[] bytes = new byte[input.remaining()];
        //    input.get(bytes);
        //    System.err.println("Client send msg -> " + new String(bytes, "utf-8"));
        //    if (inputIsComplete()) {
        //        process();
        //        state = SENDING;
        //        // Normally also do first write now
        //        selectionKey.interestOps(SelectionKey.OP_WRITE);
        //    }
        //}
        //
        //private void send() throws IOException {
        //    socket.write(output);
        //    if (outputIsComplete()) {
        //        selectionKey.cancel();
        //    }
        //}
    }
}
