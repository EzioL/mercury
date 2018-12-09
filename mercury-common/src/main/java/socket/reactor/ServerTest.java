package socket.reactor;

import java.io.IOException;
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
 * Created by @author Ezio on 2018/12/9 8:42 PM
 */
public class ServerTest implements Runnable {

    public static void main(String[] args) {
        ServerTest serverTest = new ServerTest();
        serverTest.run();
    }

    public ServerTest() {
        try {
            selector = Selector.open();

            ServerSocketChannel server = ServerSocketChannel.open();
            server.socket().bind(new InetSocketAddress(9999), 1024);
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Selector selector = null;

    @Override
    public void run() {
        while (true) {
            try {
                // 阻塞selector
                selector.select(1000);
                // ================如果有新连接 // 获得事件集合;
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                // ================遍历selectedKeys
                Iterator<SelectionKey> iterator = selectedKeys.iterator();
                SelectionKey key;
                while (iterator.hasNext()) {
                    key = iterator.next();
                    // ===============处理事件
                    handle(key);
                    // ===============
                    iterator.remove(); // 移除事件
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void handle(SelectionKey key) {
        try {
            // 连接就绪
            if (key.isAcceptable()) {
                handleAcceptable(key);
            }
            // 读就绪
            if (key.isReadable()) {
                handelReadable(key);
            }
        } catch (IOException e) {
            key.cancel();
            if (key.channel() != null) {
                try {
                    key.channel().close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    // 处理读事件
    public static void handelReadable(SelectionKey key) throws IOException {

        SocketChannel socketChannel = (SocketChannel) key.channel();
        // 为什么这里是socketChannel
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        String content = System.currentTimeMillis() + " 客户端发送了: ";
        int readBytes = socketChannel.read(byteBuffer);
        if (readBytes > 0) {
            byteBuffer.flip();
            byte[] bytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytes);
            content += new String(bytes);
            content += "__________";
            doWrite(socketChannel, content);
        }
    }

    // 处理连接事件
    public static void handleAcceptable(SelectionKey key) throws IOException {

        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverSocketChannel.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
        // 将key对应Channel设置为准备接受其他请求
        key.interestOps(SelectionKey.OP_ACCEPT);
    }

    private static void doWrite(SocketChannel socketChannel, String data) throws IOException {
        byte[] req = data.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(req.length);
        byteBuffer.put(req);
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
        if (!byteBuffer.hasRemaining()) {
            System.out.println(data + "   send success");
        }
    }
}
