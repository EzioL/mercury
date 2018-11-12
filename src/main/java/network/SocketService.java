package network;

import cache.CommandService;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
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

    private CommandService commandService;

    /**
     * 初始化socket服务
     */
    public void init() {

        try {
            commandService = new CommandService();
            serverSocket = new ServerSocket(SERVICE_PORT);
            System.out.println("启动服务器....");


            Socket s = serverSocket.accept();
            System.out.println("客户端:" + InetAddress.getLocalHost() + "已连接到服务器");
            String response = commandService.getResponse(s.getInputStream());

            //BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            ////读取客户端发送来的消息
            //StringBuilder mess = new StringBuilder();
            //String line = null;
            //// 获取客户端的信息
            //while ((line = br.readLine()) != null) {
            //    mess.append(line);
            //}
            //System.out.println("客户端：" + mess.toString());
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            bw.write(response + "\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //while (true) {
        //    service();
        //}
    }

    private void service() {
        Socket socket = null;
        try {
            socket = serverSocket.accept();//从连接队列中取出一个连接，如果没有则等待

            System.out.println("客户端:" + InetAddress.getLocalHost() + "已连接到服务器");

            //接收和发送数据
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            List<String> lines = Lists.newArrayList();
            // 获取客户端的信息
            while ((line = reader.readLine()) != null) {
                System.out.println("接收到客户端的服务请求:" + line);
                lines.add(line);
            }

            String response = commandService.getResponse(lines);
            System.out.println("服务端的执行结果:" + response);
            //按照规则解析
            OutputStream os = socket.getOutputStream();
            os.write(response.getBytes());
            os.flush();
            os.close();

            reader.close();
            socket.close();
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
