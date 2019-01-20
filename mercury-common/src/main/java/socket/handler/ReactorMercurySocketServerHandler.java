package socket.handler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

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
            selectionKey.attach(new Acceptor());
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
        private final int MAXIN = 10;
        private final int MAXOUT = 1024;
        private int read = -1;
        private String request = new String();

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
            return read == 0;
        }

        boolean outputIsComplete() {

            return true;
        }

        @Override
        public void run() {
            try {
                while (!inputIsComplete()) {
                    // =====取出buffer里的数据
                    read = socket.read(input);
                    input.flip();
                    // 创建字节数组
                    byte[] bytes = new byte[input.remaining()];
                    // 将数据取出放到字节数组里
                    input.get(bytes);
                    input.clear();
                    request += new String(bytes, StandardCharsets.UTF_8);
                }
               // String handle = serverHandler.handle(serverHandler.transformRequest(request));
               // process(handle);
                selectionKey.attach(new Handler.Sender());
                selectionKey.interestOps(SelectionKey.OP_WRITE);
                selectionKey.selector().wakeup();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void process(String handle) {

            //String content = System.currentTimeMillis() + " 来自客户端的: ";
            //input.flip();
            //// =====取出buffer里的数据// 创建字节数组
            //byte[] bytes = new byte[input.remaining()];
            //// 将数据取出放到字节数组里
            //input.get(bytes);
            //content += new String(bytes, "UTF-8");
            //content += "--------";
            //System.out.println(content);

            byte[] resp = handle.getBytes(StandardCharsets.UTF_8);

            output = ByteBuffer.wrap(resp);
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
    }
}
