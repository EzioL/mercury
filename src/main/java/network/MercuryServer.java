package network;

import com.google.gson.Gson;
import common.domain.Resp;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import network.common.MercuryCommand;
import network.common.MercuryPacket;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * Here be dragons
 * Created by haotian on 2018/11/20 12:40 PM
 */
public class MercuryServer {

    public static void main(String[] args) throws IOException {

        init();
    }

    private static void init() throws IOException {
        ServerSocket serverSocket = new ServerSocket(9103);

        BufferedSource reader;
        BufferedSink writer;
        boolean flag = true;
        while (true) {
            Socket socket = serverSocket.accept();

            try {
                System.out.println("accept: " + socket);

                reader = Okio.buffer(Okio.source(socket.getInputStream()));
                writer = Okio.buffer(Okio.sink(socket.getOutputStream()));

                MercuryPacket packet = MercuryPacket.build(reader);
                handleRequest(packet, writer);
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
    }

    private static void handleRequest(MercuryPacket packet, BufferedSink writer) throws IOException {
        System.out.println("handleRequest: " + packet);
        MercuryCommand cmd = MercuryCommand.fromValue(packet.cmd);

        switch (cmd) {
            case HEARTBEAT: {

                packet.setResponse(new Gson().toJsonTree(Resp.success("Hello")).getAsJsonObject());

                writePacket(packet, writer);
            }
            break;
        }
    }

    private static void writePacket(MercuryPacket packet, BufferedSink writer) throws IOException {
        System.out.println("write packet: " + packet);
        writer.write(packet.toBytes());
        writer.flush();
    }
}
