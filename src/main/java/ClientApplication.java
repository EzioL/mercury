import bizsocket.core.Configuration;
import java.util.concurrent.TimeUnit;
import network.MercuryClient;

/**
 * Here be dragons
 * Created by haotian on 2018/11/7 6:27 PM
 * 快乐启动器 项目入口
 */
public class ClientApplication {

    public static void main(String[] args) throws InterruptedException {
        Configuration configuration = new Configuration.Builder()
            .host("127.0.0.1")
            .port(9103)
            .readTimeout(TimeUnit.MINUTES, 2)
            .heartbeat(120)
            .build();
    //
    //    Interceptor interceptor = new Interceptor() {
    //        @Override
    //        public boolean postRequestHandle(RequestContext context) throws Exception {
    //           // System.out.println("发现一个请求postRequestHandle: " + context);
    //            return false;
    //        }
    //
    //        @Override
    //        public boolean postResponseHandle(int command, Packet responsePacket) throws Exception {
    //           // System.out.println("收到一个包postResponseHandle: " + responsePacket);
    //            return false;
    //        }
    //    };
        MercuryClient client = new MercuryClient(configuration);
    //    client.getInterceptorChain().addInterceptor(interceptor);
    //    try {
    //        //连接
    //        client.connect();
    //        //启动断线重连
    //        client.getSocketConnection().bindReconnectionManager();
    //        //开启心跳
    //        client.getSocketConnection().startHeartBeat();
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //    }
    //
    //
    //    ResponseHandler responseHandler = new ResponseHandler() {
    //        @Override
    //        public void sendSuccessMessage(int command, ByteString requestBody, Packet responsePacket) {
    //            System.err.println("cmd: " + command + " ,requestBody: " + requestBody + " attach: " + " responsePacket: " + responsePacket);
    //        }
    //
    //        @Override
    //        public void sendFailureMessage(int command, Throwable error) {
    //            System.err.println(command + " ,err: " + error);
    //        }
    //    };
    ////
    //    client.request(new Request.Builder().command(MercuryCommand.PUT_DATA.getValue())
    //        .utf8body(new Gson().toJson(new MercuryPutDataRequest("myKey", "myValue")))
    //        .build(), responseHandler);

    //    Thread.sleep(2000L);
    //
    //    client.request(new Request.Builder().command(MercuryCommand.GET_DATA.getValue())
    //        .utf8body(new Gson().toJson(new MercuryGetDataRequest("myKey")))
    //        .build(), responseHandler);
    //
    //    Thread.sleep(2000L);
    //
    //    client.request(new Request.Builder().command(MercuryCommand.DELETE_DATA.getValue())
    //        .utf8body(new Gson().toJson(new MercuryDeleteDataRequest("myKey")))
    //        .build(), responseHandler);
    //    Thread.sleep(2000L);
    //
    //    client.request(new Request.Builder().command(MercuryCommand.GET_DATA.getValue())
    //        .utf8body(new Gson().toJson(new MercuryGetDataRequest("myKey")))
    //        .build(), responseHandler);
    //
    //    Thread.sleep(2000L);
    //    client.disconnect();
    //}
    //
    //private static void init() {
    //    try {
    //        Socket socket = new Socket("127.0.0.1", 5489);
    //
    //        String cmd = "*\r\n$HELLO\r\n";
    //
    //        // String cmd = "*\r\n$NEW_TABLE\r\nccc\r\n";
    //
    //        OutputStream os = socket.getOutputStream();
    //        os.write(cmd.getBytes());
    //        os.flush();
    //
    //        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    //        String response = reader.readLine();
    //        reader.close();
    //        os.close();
    //        socket.close();
    //
    //        System.err.println(response);
    //    } catch (IOException e) {
    //
    //        e.printStackTrace();
    //    }
    }
}
