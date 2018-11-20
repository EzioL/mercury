package network;

import cache.CacheDriver;
import com.google.gson.Gson;
import common.domain.Resp;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import network.common.MercuryCommand;
import network.common.MercuryPacket;
import network.domain.MercuryDeleteDataRequest;
import network.domain.MercuryGetDataRequest;
import network.domain.MercuryPutDataRequest;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * Here be dragons
 * Created by haotian on 2018/11/20 12:40 PM
 */
public class MercuryServer {

    private static final List<ConnectThread> connectThreads = new CopyOnWriteArrayList<ConnectThread>();

    public static void main(String[] args) throws IOException {

        init();
    }

    public static void init() throws IOException {
        ServerSocket serverSocket = new ServerSocket(9103);

        boolean flag = true;
        while (flag) {
            Socket socket = serverSocket.accept();
            ConnectThread connectThread = new ConnectThread(socket);
            connectThread.start();
        }
    }

    private static class ConnectThread extends Thread {
        Socket socket;
        boolean isRunning = true;
        BufferedSource reader;
        BufferedSink writer;

        public ConnectThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            connectThreads.add(this);
            try {
                System.out.println("accept: " + socket);

                reader = Okio.buffer(Okio.source(socket.getInputStream()));
                writer = Okio.buffer(Okio.sink(socket.getOutputStream()));
                while (isRunning) {

                    try {
                        MercuryPacket packet = MercuryPacket.build(reader);
                        handleRequest(packet);
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    socket = null;
                }

                connectThreads.remove(this);
            }
        }

        private void handleRequest(MercuryPacket packet) throws IOException {
            System.out.println("handleRequest: " + packet);
            MercuryCommand cmd = MercuryCommand.fromValue(packet.cmd);

            switch (cmd) {
                case HEARTBEAT:
                    packet.setResponse(new Gson().toJsonTree(Resp.success("Hello")).getAsJsonObject());
                    writePacket(packet);
                    break;

                case PUT_DATA:
                    MercuryPutDataRequest putDataRequest = new Gson().fromJson(packet.content, MercuryPutDataRequest.class);
                    CacheDriver.setData(putDataRequest.getKey(), putDataRequest.getValue());
                    packet.setResponse(new Gson().toJsonTree(Resp.success("put success")).getAsJsonObject());
                    writePacket(packet);
                    break;

                case GET_DATA:
                    MercuryGetDataRequest getDataRequest = new Gson().fromJson(packet.content, MercuryGetDataRequest.class);
                    String value = CacheDriver.getData(getDataRequest.getKey());
                    packet.setResponse(new Gson().toJsonTree(Resp.success(value)).getAsJsonObject());
                    writePacket(packet);
                    break;

                case DELETE_DATA:
                    MercuryDeleteDataRequest deleteDataRequest = new Gson().fromJson(packet.content, MercuryDeleteDataRequest.class);
                    CacheDriver.del(deleteDataRequest.getKey());
                    packet.setResponse(new Gson().toJsonTree(Resp.success("delete success")).getAsJsonObject());
                    writePacket(packet);
                    break;
            }
        }

        private void writePacket(MercuryPacket packet) throws IOException {
            System.out.println("write packet: " + packet);
            writer.write(packet.toBytes());
            writer.flush();
        }
    }
}
