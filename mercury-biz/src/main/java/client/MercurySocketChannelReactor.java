package client;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import tcp.MercuryRequest;
import tcp.MercuryResponseHandler;


/**
 * Here be dragons Created by @author Ezio on 2019-01-20 00:42
 */
public class MercurySocketChannelReactor implements Runnable {

    private MercuryResponseHandler responseHandler;

    private MercuryClientConfiguration configuration;

    private static Selector selector;

    private static SocketChannel channel;

    private MercuryRequest request;

    private static boolean stop = false;

    public MercurySocketChannelReactor(MercuryClientConfiguration configuration, MercuryRequest request, MercuryResponseHandler responseHandler) {
        this.configuration = configuration;
        this.responseHandler = responseHandler;
        this.request = request;
    }

    @Override
    public void run() {
        try {
            // 初始化selector
            selector = Selector.open();
            // 初始化serverSocketChannel
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            if (channel.isConnectionPending()) {
                channel.finishConnect();
            }
            channel.connect(new InetSocketAddress(configuration.getHost(), configuration.getPort()));
            channel.register(selector, SelectionKey.OP_CONNECT);
        } catch (ClosedChannelException e) {
            System.err.println("Client -> 失去主机连接");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while (!stop) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    handle(key);
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handle(SelectionKey key) {
        try {
            // 连接就绪
            if (key.isConnectable()) {
                handleConnectable(key);
            }
            // 读就绪
            if (key.isReadable()) {
                handelReadable(key);
            }
        } catch (Exception e) {
            key.cancel();
            if (key.channel() != null) {
                try {
                    key.channel().close();
                } catch (IOException e1) {
                }
            }
        }
    }

    private void handleConnectable(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        if (socketChannel.finishConnect()) {
            // 将关注的事件变成read
            socketChannel.register(selector, SelectionKey.OP_READ);
            //  doWrite(socketChannel, "Hello Server");
            doWrite(socketChannel, new Gson().toJson(request));
        }
    }

    private void handelReadable(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 从channel读到buffer
        int temp = sc.read(buffer);
        String content = System.currentTimeMillis() + " 来自服务端的: ";
        // 代表读完毕了,准备写(即打印出来)
        if (temp > 0) {
            // 为write()准备
            buffer.flip();

            // =====取出buffer里的数据// 创建字节数组
            byte[] bytes = new byte[buffer.remaining()];
            // 将数据取出放到字节数组里
            buffer.get(bytes);
            content += new String(bytes, StandardCharsets.UTF_8);

            System.out.println(content);
            doWrite(sc, content);
        }
    }

    private void doWrite(SocketChannel socketChannel, String data) throws IOException {

        byte[] req = data.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.wrap(req);
        byteBuffer.put(req);
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
    }
}
