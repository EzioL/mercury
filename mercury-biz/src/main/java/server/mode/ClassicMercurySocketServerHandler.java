package server.mode;

import com.google.gson.Gson;
import db.CacheDriver;
import domain.MercuryDeleteDataRequest;
import domain.MercuryGetDataRequest;
import domain.MercuryPutDataRequest;
import entity.MercuryCommand;
import entity.MercuryPacket;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import resp.Resp;
import server.handler.MercuryServerHandler;
import server.handler.MercurySocketServerHandler;

/**
 * Here be dragons Created by haotian on 2018/11/23 6:47 PM
 *
 * @author haotian
 */
public class ClassicMercurySocketServerHandler extends MercurySocketServerHandler {

    private ServerSocket mercuryServerSocket;

    private MercuryServerHandler mercurySocketHandler;

    private int port;

    public ClassicMercurySocketServerHandler(int port, MercuryServerHandler handler) {
        this.port = port;
        this.mercurySocketHandler = handler;
    }

    @Override
    public void run() {
        try {
            System.out.println("ClassicMercurySocketServerHandler running ");
            mercuryServerSocket = new ServerSocket(port);
            boolean flag = true;
            while (flag) {
                Socket socket = mercuryServerSocket.accept();
                ConnectThread connectThread = new ConnectThread(socket, mercurySocketHandler);
                connectThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ConnectThread extends Thread {
        Socket socket;
        boolean isRunning = true;
        BufferedSource reader;
        BufferedSink writer;
        private MercuryServerHandler handler;

        public ConnectThread(Socket socket, MercuryServerHandler handler) {
            this.socket = socket;
            this.handler = handler;
        }

        @Override
        public void run() {
            try {
                System.out.println(" ClassicMercurySocketServerHandler accept: " + socket);
                reader = Okio.buffer(Okio.source(socket.getInputStream()));
                writer = Okio.buffer(Okio.sink(socket.getOutputStream()));
                while (isRunning) {
                    try {

                        reader = Okio.buffer(Okio.source(socket.getInputStream()));
                        writer = Okio.buffer(Okio.sink(socket.getOutputStream()));
                        while (isRunning) {

                            MercuryPacket packet = MercuryPacket.build(reader);
                            MercuryPacket mercuryPacket = handler.transformRequest(packet);
                            handleRequest(mercuryPacket);
                            Thread.sleep(500);
                        }

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
            }
        }

        private void handleRequest(MercuryPacket packet) throws IOException {
            System.out.println("handleRequest: " + packet);
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

            writePacket(handler.transformResponse(packet));
        }

        private void writePacket(MercuryPacket packet) throws IOException {
            System.out.println("write packet: " + packet);
            writer.write(packet.toBytes());
            writer.flush();
        }
    }

    //
}
