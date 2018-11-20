package network;

import cache.CacheCommandService;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Here be dragons
 * Created by haotian on 2018/11/11 6:45 PM
 */
public class SocketService {

    private static final int SERVICE_PORT = 5489;

    // socket服务
    private static ServerSocket serverSocket;

    private CacheCommandService commandService;

    /**
     * 初始化socket服务
     */
    public void init() {

        try {
            commandService = new CacheCommandService();
            serverSocket = new ServerSocket(SERVICE_PORT);
            System.out.println("启动服务器....");

            Socket socket = serverSocket.accept();//从连接队列中取出一个连接，如果没有则等待
            System.out.println("客户端:" + InetAddress.getLocalHost() + "已连接到服务器");
            Socket s = serverSocket.accept();
            System.out.println("客户端:" + InetAddress.getLocalHost() + "已连接到服务器");
            String response = commandService.getResponse(s.getInputStream());
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            bw.write(response + "\n");
            bw.flush();
            //while (true) {
            //    service();
            //}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void service() {
        Socket socket = null;
        try {
            socket = serverSocket.accept();//从连接队列中取出一个连接，如果没有则等待
            System.out.println("客户端:" + InetAddress.getLocalHost() + "已连接到服务器");
            Socket s = serverSocket.accept();
            System.out.println("客户端:" + InetAddress.getLocalHost() + "已连接到服务器");
            String response = commandService.getResponse(s.getInputStream());
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            bw.write(response + "\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();//与一个客户端通信结束后，要关闭Socket
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
