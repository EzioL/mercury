package network;

import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Here be dragons
 * Created by haotian on 2018/11/11 6:45 PM
 */
public class SocketService {

    private static final int SERVICE_PORT = 5489;

    // socket服务
    private static ServerSocket serverSocket;

    /**
     * 初始化socket服务
     */
    public void init() {
        try {
            serverSocket = new ServerSocket(SERVICE_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            service();
        }
    }

    private void service() {
        Socket socket = null;
        try {
            socket = serverSocket.accept();//从连接队列中取出一个连接，如果没有则等待
            //接收和发送数据
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            List<String> lines = Lists.newArrayList();
            // 获取客户端的信息
            while ((line = reader.readLine()) != null) {
                System.out.println("接收到客户端的服务请求:" + line);
                lines.add(line);
            }
            //按照规则解析



            socket.shutdownInput();
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
