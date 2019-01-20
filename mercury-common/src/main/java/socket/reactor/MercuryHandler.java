package socket.reactor;

import db.CacheDriver;
import com.google.gson.Gson;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import resp.Resp;
import socket.domain.MercuryDeleteDataRequest;
import socket.domain.MercuryGetDataRequest;
import socket.domain.MercuryPutDataRequest;
import socket.entity.MercuryCommand;
import socket.entity.MercuryPacket;
import socket.handler.MercuryServerHandler;

/**
 * Here be dragons Created by @author Ezio on 2019-01-19 22:45
 */
public class MercuryHandler implements Runnable {

    private Selector selector;

    private SocketChannel socket;

    private SelectionKey selectionKey;

    private MercuryServerHandler serverHandler;

    private final int MAXIN = 10;
    private final int MAXOUT = 1024;
    private int read = -1;
    private int write = -1;

    ByteBuffer input = ByteBuffer.allocate(MAXIN);

    ByteBuffer output = ByteBuffer.allocate(MAXOUT);

    String request = "";

    MercuryHandler(Selector selector, SocketChannel socketChannel, MercuryServerHandler serverHandler) throws IOException {
        this.socket = socketChannel;
        this.serverHandler = serverHandler;

        socketChannel.configureBlocking(false);
        selectionKey = socketChannel.register(selector, 0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    @Override public void run() {

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

            MercuryPacket mercuryPacket = new Gson().fromJson(request, MercuryPacket.class);

            process(serverHandler.transformRequest(mercuryPacket));

            selectionKey.attach(new Sender());
            selectionKey.interestOps(SelectionKey.OP_WRITE);
            selectionKey.selector().wakeup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void process(MercuryPacket packet) {
        System.out.println("handle process: " + packet);
        MercuryCommand cmd = MercuryCommand.fromValue(packet.cmd);

        switch (cmd) {
            case HEARTBEAT:
                packet.setResponse(new Gson().toJsonTree(Resp.success("Hello")).getAsJsonObject());
                break;

            case PUT_DATA:
                MercuryPutDataRequest putDataRequest = new Gson().fromJson(packet.content, MercuryPutDataRequest.class);
                CacheDriver.setData(putDataRequest.getKey(), putDataRequest.getValue());
                packet.setResponse(new Gson().toJsonTree(Resp.success("put success")).getAsJsonObject());
                break;

            case GET_DATA:
                MercuryGetDataRequest getDataRequest = new Gson().fromJson(packet.content, MercuryGetDataRequest.class);
                String value = CacheDriver.getData(getDataRequest.getKey());
                packet.setResponse(new Gson().toJsonTree(Resp.success(value)).getAsJsonObject());
                break;
            case DELETE_DATA:
                MercuryDeleteDataRequest deleteDataRequest = new Gson().fromJson(packet.content, MercuryDeleteDataRequest.class);
                CacheDriver.del(deleteDataRequest.getKey());
                packet.setResponse(new Gson().toJsonTree(Resp.success("delete success")).getAsJsonObject());
                break;
            case NOTIFY_DISCONNECT:
                packet.setResponse(new Gson().toJsonTree(Resp.success("Bye")).getAsJsonObject());
                break;
        }

        byte[] resp = serverHandler.transformResponse(packet).toString().getBytes(StandardCharsets.UTF_8);

        output = ByteBuffer.wrap(resp);
    }

    private boolean inputIsComplete() {
        System.err.println("read state ->" + read);
        return read == 0;
    }

    //private boolean outputIsComplete() {
    //
    //    System.err.println("write state ->" + write);
    //    return write == 0;
    //}

    class Sender implements Runnable {

        @Override
        public void run() {

            try {
                socket.write(output);
                selectionKey.cancel();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
