package socket.handler;

import java.io.IOException;
import java.net.Socket;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import socket.domain.MercuryServerSocket;

/**
 * Here be dragons
 * Created by haotian on 2018/11/23 6:47 PM
 *
 * @author haotian
 */
public class ClassicMercurySocketServerHandler extends MercurySocketServerHandler {

    private MercuryServerSocket mercuryServerSocket;

    private MercurySocketHandler mercurySocketHandler;

    private int port;

    public ClassicMercurySocketServerHandler(int port, MercurySocketHandler handler) {
        this.port = port;
        this.mercurySocketHandler = handler;
    }

    @Override
    public void run() {
        try {
            mercuryServerSocket = new MercuryServerSocket(port);
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
        private MercurySocketHandler handler;

        public ConnectThread(Socket socket, MercurySocketHandler handler) {
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
                        handler.handle(socket);
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
    }
}
